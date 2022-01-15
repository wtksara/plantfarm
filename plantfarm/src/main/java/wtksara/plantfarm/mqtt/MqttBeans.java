package wtksara.plantfarm.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import wtksara.plantfarm.measurement.MeasurementController;
import wtksara.plantfarm.tank.TankController;

@Configuration
// Konfiguracja komunikacji z wykorzystaniem MQTT
public class MqttBeans {

    @Autowired
    private MeasurementController measurementController;

    @Autowired
    private TankController tankController;

    @Bean
    // Uworzenie klienta MQTT
    public MqttPahoClientFactory mqttClientFactory(){
        // Utworzenie klienta niezbędnego to konfiguracji obu adapterów
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        // Ustawienie własciwości połaczenia z brokerem MQTT.
        options.setServerURIs(new String [] {"tcp://192.168.1.20:1883"});
        // Zapamietywanie stanu po uruchomieniu i ponownym połączeniu klienta oraz serwera
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    // Odbiorca wiadomości
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    // Konfiguracja adaptera wiadomości przychodzących
    public MessageProducer inbound() {
        // Określenie listy tematów, które bedą subskrybowane
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "patch/#", "waterPump/measurements/#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        // Określenie jakości obsługi
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    // Metoda używana do przetwarzania wiadomości w ramach protokołu MQTT. Parametr inputChannel określa kanał używany do wiadomości przychodzących
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            // Obsługa przychodzących wiadomości
            public void handleMessage(Message<?> message) throws MessagingException {

                // Odczytanie nazwy tematu na który została nadana wiadomości
                // W zależności od tematu przetwarzanie odbywa się w inny sposób
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                // Temat "patch" oznacza że wiadomości są nadawane przez plantacje
                if(topic.startsWith("patch")) {
                    // Jeśli wiadomość rozpoczyna się od "connected" oznacza to że urządzenie nazwiązało połaczenie
                    if (!message.getPayload().toString().startsWith("connected")) {
                        // Jeśli nie, to oznacza że urządzenie przesyła w wiadomości sporzadzone pomiary
                        // Odczytanie z tematu numeru plantacji
                        Long patchId = Long.parseLong(topic.replaceAll("[^0-9]", ""));
                        // Odczytanie z wiadomości poszczególnych pomiarów
                        String[] values = message.getPayload().toString().split(";");
                        double humidity = 0;
                        // Dostosowanie wartości pomiaru wilgotności
                        if (Integer.parseInt(values[0]) < 300){
                            humidity = 100;
                        }
                        else if (Integer.parseInt(values[0]) > 550) {
                            humidity = 0;
                        }
                        else {
                            humidity = (1.00-((Double.parseDouble(values[0]) - 300.00)/250.00))*100.00;
                        }
                        // Zapisanie pomiaru dla wybranej uprawy na danej plantajci
                        measurementController.createMeasurement(patchId,  humidity, Double.parseDouble(values[1]));
                    }
                }
                // Temat "waterPump" oznacza że wiadomości są nadawane przez pompe wody
                else if(topic.startsWith("waterPump")) {
                    // Jeśli wiadomość rozpoczyna się od "connected" oznacza to że urządzenie nazwiązało połaczenie
                    if (!message.getPayload().toString().startsWith("connected")) {
                        // Jeśli nie, to oznacza że urządzenie przesyła w wiadomości sporzadzone pomiary
                        // Odczytanie z tematu numeru zbiornika
                        Long tankId = Long.parseLong(topic.replaceAll("[^0-9]", ""));
                        // Odczytanie z wiadomości poszczególnych pomiarów
                        String[] values = message.getPayload().toString().split(";");
                        // Dostosowanie wartości pomiaru poziomu wody w zbiorniku
                        Integer valueOfTank = Integer.parseInt(values[0]);
                        Integer level;
                        if (valueOfTank > 30){
                            level = 0 ;
                        }
                        else if ( valueOfTank < 6 ){
                            level = 100;
                        }
                        else {
                            level =  (int) ((1.00 - ((Double.valueOf(valueOfTank) - 6.00) / 24.00)) * 100 );
                        }
                        // Dostosowanie wartości poziomu nasłonecznienia
                        Integer valueOfInsolation= Integer.parseInt(values[1]);
                        Integer insolation;
                        if (valueOfInsolation > 1024){
                            insolation = 0;
                        }
                        else if (valueOfInsolation < 500 ){
                            insolation = 100;
                        }
                        else {
                            insolation = (int) ((1.00 - ((Double.valueOf(valueOfInsolation) - 500.00) / 524.00)) * 100 );
                        }
                        // Zapisanie stanu zbiornika
                        tankController.updateTank(tankId, level, insolation);
                    }
                }
            }
        };
    }

    @Bean
    // Producent wiadomości
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    // Obsługa kanału wychodzącego
    public MessageHandler mqttOutbound() {
        // Konfiguracja adaptera kanału wychodzącego
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        // Obsługa asynchronicznych operacji wysyłania, aby uniknać blokowania przed potwierdzeniem dostarczenia
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        // Określenie jakości obsługi
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }
}

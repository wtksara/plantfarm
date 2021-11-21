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
import wtksara.plantfarm.patch.PatchController;
import wtksara.plantfarm.plant.PlantService;
import wtksara.plantfarm.tank.TankController;

@Configuration
public class MqttBeans {

    @Autowired
    private MeasurementController measurementController;

    @Autowired
    private TankController tankController;

    @Bean
    public MqttPahoClientFactory mqttClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String [] {"tcp://192.168.1.43:1883"});
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "patch1/measurements", "patch2/measurements", "waterPump1/measurements");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if(topic.startsWith("patch")) {
                    if (!message.getPayload().toString().startsWith("connected")) {
                        Long patchId = Long.parseLong(topic.replaceAll("[^0-9]", ""));
                        String[] values = message.getPayload().toString().split(";");
                        System.out.println(values[0]);
                        System.out.println(values[1]);

                        measurementController.createMeasurement(patchId, Double.parseDouble(values[0]), Double.parseDouble(values[1]));
                    }
                }
                else if(topic.startsWith("waterPump")) {
                    if (!message.getPayload().toString().startsWith("connected")) {
                        Long tankId = Long.parseLong(topic.replaceAll("[^0-9]", ""));
                        Integer value = Integer.parseInt(message.getPayload().toString());
                        System.out.println(value / 2);

                        tankController.updateTank(tankId, (value / 2));
                    }
                }
            }
        };
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        //clientId is generated using a random number
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

}

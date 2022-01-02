package wtksara.plantfarm.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

// Konfiguracja wysyłania wiadomości
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    // Można wysyłać wiadomości na różne tematy, jeśli jednak temat nie zostanie określony to wybierany jest domyślny.
    // Zmienna data zawiera treść wiadomości do wysłania, a topic to nazwę tematu
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}
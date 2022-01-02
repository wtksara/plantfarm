package wtksara.plantfarm.tank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.exception.ResourceNotFoundException;
import wtksara.plantfarm.mqtt.MqttGateway;

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class TankController {

    // Wstrzykiwanie zależności, a dokładniej repozytorium tankRepository
    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private MqttGateway mqttGateway;

    @GetMapping("/tank/{id}")
    // Metoda zwracająca aktualny poziom wody dla danego zbiornika
    public ResponseEntity<Tank> getTank(@PathVariable Long id){
        Tank tank = tankRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tank not exist with id" + id) );
        return ResponseEntity.ok(tank);
    }

    // Metoda umożliwiająca aktualizacje aktualnego poziomu wody w zbiorniku
    public void updateTank(Long id, Integer value){
        Tank tank = tankRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tank not exist with id" + id) );
        tank.setLevel(value);
        tankRepository.save(tank);
    }

    @PostMapping("/tank/{id}")
    // Metoda wysylajaca na dany temat wiadomosc o checi rozpoczecia podlewania
    public ResponseEntity <?> waterPants(@PathVariable Long id){
        String topic = "waterPump/" + id.toString() + "/watering";

       try {
           // Próba wykonanania podlania
           mqttGateway.sendToMqtt("on", "waterPump1/watering");
       }
       catch (Exception ex) {
        ex.printStackTrace();
        throw new ResourceNotFoundException("Watering Failed");
       }
        return ResponseEntity.ok("Watering Success");
    }



}

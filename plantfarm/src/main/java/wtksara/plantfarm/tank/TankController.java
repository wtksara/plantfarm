package wtksara.plantfarm.tank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.exception.ResourceNotFoundException;
import wtksara.plantfarm.mqtt.MqttGateway;
import wtksara.plantfarm.plant.Plant;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class TankController {

    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private MqttGateway mqttGateway;

    // Get tank
    @GetMapping("/tank/{id}")
    public ResponseEntity<Tank> getTank(@PathVariable Long id){
        Tank tank = tankRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tank not exist with id" + id) );
        return ResponseEntity.ok(tank);
    }

    public void updateTank(Long id, Integer value){
        Tank tank = tankRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tank not exist with id" + id) );
        tank.setLevel(value);
        tankRepository.save(tank);
    }

    // Water plants
    @PostMapping("tank/watering")
    public ResponseEntity <?> waterPants(){
       try {
           mqttGateway.sendToMqtt("on", "waterPump1/watering");
           return ResponseEntity.ok("Watering Success");
       }
       catch (Exception ex) {
        ex.printStackTrace();
        throw new ResourceNotFoundException("Watering Failed");
       }
    }



}

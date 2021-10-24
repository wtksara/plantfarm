package wtksara.plantfarm.tank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtksara.plantfarm.exception.ResourceNotFoundException;
import wtksara.plantfarm.plant.Plant;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class TankController {

    @Autowired
    private TankRepository tankRepository;

    // Get tank
    @GetMapping("/tank")
    public ResponseEntity<Tank> getTank(){
        Long id = Long.valueOf(1);
        Tank tank = tankRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Tank not exist with id" + id) );
        return ResponseEntity.ok(tank);
    }
}

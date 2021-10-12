package wtksara.plantfarm.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    // Get list of all plants
    @GetMapping("/plants")
    public List<Plant> getAllPlants(){
        return plantRepository.findAll();
    }

    @PostMapping("/plants")
    // Add new plant
    public Plant createPlant(@RequestBody Plant plant){
        return plantRepository.save(plant);
    }
}

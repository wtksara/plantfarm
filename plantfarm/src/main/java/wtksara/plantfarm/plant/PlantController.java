package wtksara.plantfarm.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    // Get list of all plants
    @GetMapping("/plants")
    public List<Plant> getAllPlants(){
        return plantRepository.findAllByOrderByIdAsc();
    }

    // Add new plant
    @PostMapping("/plants")
    public Plant createPlant(@RequestBody Plant plant){
        return plantRepository.save(plant);
    }

    // Get plant by id
    @GetMapping("/plants/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        return ResponseEntity.ok(plant);
    }

    // Update plant details
    @PutMapping("/plants/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable Long id, @RequestBody Plant plantDetails){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );

        plant.setName(plantDetails.getName());
        plant.setType(plantDetails.getType());
        plant.setPhoto(plantDetails.getPhoto());
        plant.setHumidity(plantDetails.getHumidity());
        plant.setTemperature(plantDetails.getTemperature());
        plant.setAmountOfDays(plantDetails.getAmountOfDays());

        Plant updatedPlant = plantRepository.save(plant);
        return ResponseEntity.ok(updatedPlant);
    }

    // Delete plant
    @DeleteMapping("/plants/{id}")
    public ResponseEntity<Map<String,Boolean>> deletePlant(@PathVariable Long id){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        plantRepository.delete(plant);
        Map<String,Boolean> response = new HashMap<>();
        response.put ("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}

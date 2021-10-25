package wtksara.plantfarm.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wtksara.plantfarm.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class PlantController {

    @Autowired
    private PlantService plantService;

    // Get list of all plants
    @GetMapping("/plants")
    public List<Plant> getAllPlants() {
        return plantService.findAllByOrderByIdAsc();
    }

    // Add new plant
    @PostMapping("/plants")
    public Plant createPlant(@RequestBody Plant plant) {
        return plantService.createPlant(plant);
    }

    @PostMapping(
            path = "plants/{id}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadPlantImage(@PathVariable Long id, @RequestParam MultipartFile file) {
        plantService.uploadPlantImage(id, file);

    }

    // Get plant by id
    @GetMapping("/plants/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    // Update plant details
    @PutMapping("/plants/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable Long id, @RequestBody Plant plantDetails) {
        return plantService.updatePlant(id, plantDetails);
    }

    // Delete plant
    @DeleteMapping("/plants/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePlant(@PathVariable Long id) {
        return plantService.deletePlant(id);
    }
}
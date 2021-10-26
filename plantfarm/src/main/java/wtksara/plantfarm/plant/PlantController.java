package wtksara.plantfarm.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/plants/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadPhoto (@PathVariable ("id") Long id) {
        Plant plant = plantService.getPlantByIdToDownload(id);
        byte[] data =  plantService.downloadPhoto(id, plant.getPhoto());
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + plant.getPhoto() + "\"")
                .body(resource);
    }

    // Add new plant
    @PostMapping(path = "/plants", consumes={"multipart/form-data"}, headers={"Accept=application/json"})
    @ResponseBody
    public ResponseEntity<String> createPlant(@RequestPart("file") MultipartFile file, @RequestPart("plant") Plant plant) throws IOException {
        Long plantId = plantService.createPlant(plant).getId();
        return new ResponseEntity<>(plantService.uploadPhoto(plantId, file), HttpStatus.OK);
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
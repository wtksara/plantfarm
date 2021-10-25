package wtksara.plantfarm.plant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import wtksara.plantfarm.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public List<Plant> findAllByOrderByIdAsc(){
        return plantRepository.findAllByOrderByIdAsc();
    }

    public Plant createPlant(Plant plant){
        return plantRepository.save(plant);
    }

    public void uploadPlantImage(Long id, MultipartFile file) {
        // Check if image is not empty
        // Check if file is an image
        // Check if the user exists in our database
        // Grab some metadata from file if any
        // Store the image in S3 and update database (image) with s3 image link


    }

    public ResponseEntity<Plant> getPlantById(Long id){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        return ResponseEntity.ok(plant);
    }

    public ResponseEntity<Plant> updatePlant(Long id, Plant plantDetails){
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

    public ResponseEntity<Map<String,Boolean>> deletePlant(Long id){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        plantRepository.delete(plant);
        Map<String,Boolean> response = new HashMap<>();
        response.put ("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }


}

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

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class PlantController {

    // Wstrzykiwanie zależności, a dokładniej serwisu plantService
    @Autowired
    private PlantService plantService;

    @GetMapping("/plants")
    // Metoda zwracająca wszystkie rośliny w kolejności rosnącej po id
    public List<Plant> getAllPlants() {
        return plantService.findAllByOrderByIdAsc();
    }

    @GetMapping("/plants/type/{type}")
    // Metoda zwracająca wszystkie rośliny danego typu
    public List<Plant> getAllPlantsByTypeOrderByIdAsc(@PathVariable ("type") String type)  {
        return plantService.findAllByTypeOrderByIdAsc(type);
    }

    @GetMapping("/plants/show")
    // Metoda zwracająca trzy pierwsze rośliny w kolejności rosnącej po id
    public List<Plant> getAllShowPlants() {
        return plantService.findTop3ByOrderByIdAsc();
    }

    @GetMapping("/plants/{id}/download")
    // Metoda zwracająca zdjęcie danej rośliny
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

    // Żadanie wielocześciowe - przesyłanie grafiki oraz danych o roślinie
    @PostMapping(path = "/plants", consumes={"multipart/form-data"}, headers={"Accept=application/json"})
    @ResponseBody
    // Metoda tworząca nowa roślinę i dodającą grafikę do Amazon S3
    public ResponseEntity<String> createPlant(@RequestPart("file") MultipartFile file, @RequestPart("plant") Plant plant) throws IOException {
        Long plantId = plantService.createPlant(plant).getId();
        return new ResponseEntity<>(plantService.uploadPhoto(plantId, file), HttpStatus.OK);
    }

    @GetMapping("/plants/{id}")
    // Metoda zwracająca rośline o danym id
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id);
    }

    @PutMapping(path = "/plants", headers={"Accept=application/json"})
    // Metoda, która aktualizuje informacje o danej roślinie z wyjątkiem zmiany grafiki
    public ResponseEntity<Plant> updatePlant( @RequestPart("plant") Plant plantDetails) {
        return plantService.updatePlant(plantDetails);
    }

    // Żadanie wieloczesciowe - przesyłany nowy obraz oraz dane o roślinie
    @PutMapping(path = "/plants/all", consumes={"multipart/form-data"}, headers={"Accept=application/json"})
    // Metoda, która aktualizuje informacje o danej roślinie wraz z jej grafiką
    public ResponseEntity<String> updatePlantAndImage( @RequestPart("file") MultipartFile file, @RequestPart("plant") Plant plantDetails) {
        plantService.updatePlant(plantDetails);
        return new ResponseEntity<>(plantService.uploadPhoto(plantDetails.getId(), file), HttpStatus.OK);
    }

    @DeleteMapping("/plants/{id}")
    // Metoda usuwająca rośline o danym id
    public ResponseEntity<Map<String, Boolean>> deletePlant(@PathVariable Long id) {
        return plantService.deletePlant(id);
    }
}
package wtksara.plantfarm.plant;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wtksara.plantfarm.exception.ResourceNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

// Komponent pełni role serwisu
@Service
@Slf4j
public class PlantService {

    // Nazwa bucketu, który został stworzony do przechowywania grafik
    @Value("${application.bucket.name}")
    private String bucketName;

    // Klient używany w celu dostępu do usług Amazon S3
    @Autowired
    private AmazonS3 s3Client;

    // Wstrzykiwanie zależności, a dokładniej repozytorium plantRepository
    @Autowired
    private PlantRepository plantRepository;

    public List<Plant> findAllByOrderByIdAsc(){
        return plantRepository.findAllByOrderByIdAsc();
    }

    public List<Plant> findTop3ByOrderByIdAsc() { return plantRepository.findTop3ByOrderByIdAsc();}

    public List<Plant> findAllByTypeOrderByIdAsc(String type) {
        return plantRepository.findAllByTypeOrderByIdAsc(type);
    }

    public Plant createPlant(Plant plant){
        return plantRepository.save(plant);
    }

    // Metoda zwracajaca zdjecie danej rośliny
    public byte [] downloadPhoto(Long id, String fileName) {
        // Ustalenie odpowiedniej sciezki do pliku
        String path = String.format("%s/%s", bucketName, id);
        // Uzyskanie pliku, którego nazwa została przekazanego w zmiennej fileName
        S3Object s3Object = s3Client.getObject(path, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metoda zwracająca roślinę o danym id, jeśli istnieje
    public Plant getPlantByIdToDownload(Long id){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        return plant;
    }

    // Metoda odpowiedzialna za dodanie zdjecia
    public String uploadPhoto( Long plantId, MultipartFile file)  {
        // Sprawdzenie czy plik nie jest pusty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        // Sprawdzenie czy plik jest odpowiedniego formatu czyli czy jest grafiką
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image");
        }
        // Uzyskanie rośliny dla której ma być dodany plik
        Plant plant = plantRepository.findById(plantId).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + plantId) );

        // Konwersja pliku
        File fileObj = convertMultiPartFileToFile(file);
        // Ustalenie odpowiedniej scieżki do pliku
        String path = String.format("%s/%s", bucketName, plantId);
        // Ustalenie nazwy pliku
        String fileName = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());

        // Sprawdzenie czy do danej rośliny jest już przypisana grafika
        if (plant.getPhoto() != null) {
            // Jeśli tak to należy tą grafikę najpierw usunać
            s3Client.deleteObject(new DeleteObjectRequest(path, plant.getPhoto()));
        }
        // Dodanie pliku o ustalonej nazwie do wskazanej scieżki
        s3Client.putObject(new PutObjectRequest(path, fileName, fileObj));

        // Zapisanie ustalonej nazwy pliku do danej rośliny
        plant.setPhoto(fileName);
        plantRepository.save(plant);

        // Czyszczenie pamięci
        fileObj.delete();
        return "File uploaded : " + fileName;
    }

    // Konwersja pliku z MultipartFile do File.
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }


    public ResponseEntity<Plant> getPlantById(Long id){
        Plant plant = plantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + id) );
        return ResponseEntity.ok(plant);
    }

    public ResponseEntity<Plant> updatePlant(Plant plantDetails){
        Plant plant = plantRepository.findById(plantDetails.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Plant not exist with id" + plantDetails.getId()) );

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

        String path = String.format("%s/%s", bucketName, id);
        s3Client.deleteObject(new DeleteObjectRequest(path, plant.getPhoto()));
        plantRepository.delete(plant);

        Map<String,Boolean> response = new HashMap<>();
        response.put ("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }



}

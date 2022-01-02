package wtksara.plantfarm.cultivation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.measurement.MeasurementRepository;
import wtksara.plantfarm.plant.Plant;
import wtksara.plantfarm.plant.PlantRepository;

import java.util.List;

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class CultivationController {

    // Wstrzykiwanie zależności, a dokładniej repozytorium cultivationRepository
    @Autowired
    private CultivationRepository cultivationRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    // Mapowanie żadania GET
    @GetMapping("/cultivation")
    // Metoda zwracająca wszystkie uprawy w kolejności malejacej po id
    public List<Cultivation> getAllCultivations() {
        List<Cultivation> cultivations = cultivationRepository.findAllByOrderByIdDesc();
        cultivations.forEach((cultivation ->  cultivation.setMeasurements(measurementRepository.findAllByCultivationOrderByIdDesc(cultivation))));
        return cultivations;
    }

    // Stworzenie nowej uprawy rośliny
    public Cultivation createCultivation(Long plantId, Long patchId) {
        Cultivation cultivation = new Cultivation();
        Plant plant = plantRepository.findById(plantId).get();
        // Ustawienie typu rośliny na uprawie
        cultivation.setPlant(plant);
        // Ustawienie numeru grzadki
        cultivation.setGrowPatch(patchId);
        // Ustawienie uprawy na aktualnie działającą
        cultivation.setFinished(false);
        return cultivationRepository.save(cultivation);
    }

}

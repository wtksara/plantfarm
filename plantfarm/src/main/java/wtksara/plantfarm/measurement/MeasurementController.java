package wtksara.plantfarm.measurement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.patch.PatchRepository;
import java.util.Date;

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class MeasurementController {

    // Wstrzykiwanie zależności - repozytorium measurementRepository
    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private PatchRepository patchRepository;

    // Metoda odpowiedzialna za utworzenie nowego pomiaru dla danej uprawy
    public void createMeasurement(Long patchId, Double humidity, Double temperature) {
        // Znalezienie aktualnej uprawy na danej plantacji
        Cultivation cultivation = patchRepository.findById(patchId).get().getCultivation();
        if (cultivation != null) {
            // Utworzenie nowego pomiaru
            Measurement measurement = new Measurement();
            // Przypisanie pomiaru do uprawy
            measurement.setCultivation(cultivation);
            // Zapisanie sporzadzonych pomiarów
            measurement.setHumidity(humidity);
            measurement.setTemperature(temperature);
            // Ustawienie daty pomiaru
            Date date = new Date();
            measurement.setDate(date);
            measurementRepository.save(measurement);
        }
    }
}


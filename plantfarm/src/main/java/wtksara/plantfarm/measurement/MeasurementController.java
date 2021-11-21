package wtksara.plantfarm.measurement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.cultivation.CultivationRepository;
import wtksara.plantfarm.patch.Patch;
import wtksara.plantfarm.patch.PatchRepository;
import wtksara.plantfarm.plant.Plant;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class MeasurementController {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private PatchRepository patchRepository;

    public void createMeasurement(Long patchId, Double humidity, Double temperature) {
        Cultivation cultivation = patchRepository.findById(patchId).get().getCultivation();

        if (cultivation != null) {

            Measurement measurement = new Measurement();
            measurement.setCultivation(cultivation);
            measurement.setHumidity(humidity);
            measurement.setTemperature(temperature);

            Date date = new Date();
            measurement.setDate(date);

            measurementRepository.save(measurement);
        }
    }
}


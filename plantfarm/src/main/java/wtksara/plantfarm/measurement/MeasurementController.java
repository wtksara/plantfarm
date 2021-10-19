package wtksara.plantfarm.measurement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.cultivation.CultivationRepository;
import wtksara.plantfarm.patch.Patch;
import wtksara.plantfarm.patch.PatchRepository;
import wtksara.plantfarm.plant.Plant;

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

    // Add new plant
    @PostMapping("/measurement")
    public Measurement createMeasurement(@RequestBody Long patchId, @RequestBody Date date, @RequestBody Double humidity, @RequestBody Double temperature)
    {
        Patch patch = patchRepository.findById(patchId).get();

        Measurement measurement = new Measurement();

        measurement.setCultivation(patch.getCultivation());
        measurement.setDate(date);
        measurement.setHumidity(humidity);
        measurement.setTemperature(temperature);

        return measurementRepository.save(measurement);
    }
}


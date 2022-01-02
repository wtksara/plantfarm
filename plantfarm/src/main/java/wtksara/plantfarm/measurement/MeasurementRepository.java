package wtksara.plantfarm.measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import wtksara.plantfarm.cultivation.Cultivation;

import java.util.List;

// Repozytorium dla encji Measurement
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findAllByCultivationOrderByIdDesc(Cultivation cultivation);
}

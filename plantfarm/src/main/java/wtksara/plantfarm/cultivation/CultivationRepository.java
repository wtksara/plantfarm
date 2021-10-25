package wtksara.plantfarm.cultivation;

import org.springframework.data.jpa.repository.JpaRepository;
import wtksara.plantfarm.plant.Plant;

import java.util.List;

public interface CultivationRepository extends JpaRepository<Cultivation, Long> {

    public List<Cultivation> findAllByOrderByIdDesc();
}

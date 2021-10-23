package wtksara.plantfarm.plant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    public List<Plant> findAllByOrderByIdAsc();
}


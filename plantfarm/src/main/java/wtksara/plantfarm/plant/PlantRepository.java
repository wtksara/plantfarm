package wtksara.plantfarm.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repozytorium dla encji Plant
@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

   public List<Plant> findAllByOrderByIdAsc();

   public List<Plant> findTop3ByOrderByIdAsc();

   public List<Plant> findAllByTypeOrderByIdAsc (String type);
}


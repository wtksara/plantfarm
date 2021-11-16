package wtksara.plantfarm.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

   public List<Plant> findAllByOrderByIdAsc();

   public List<Plant> findTop3ByOrderByIdAsc();
}


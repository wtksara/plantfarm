package wtksara.plantfarm.cultivation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repozytorium dla encji Cultivation
public interface CultivationRepository extends JpaRepository<Cultivation, Long> {

    public List<Cultivation> findAllByOrderByIdDesc();
}

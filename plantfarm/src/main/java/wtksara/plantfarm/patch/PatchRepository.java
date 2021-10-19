package wtksara.plantfarm.patch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wtksara.plantfarm.plant.Plant;

import java.util.List;

public interface PatchRepository extends JpaRepository<Patch, Long> {

    List<Patch> getAllPatchesDetails();
}


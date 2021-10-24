package wtksara.plantfarm.patch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wtksara.plantfarm.plant.Plant;
import wtksara.plantfarm.response.PatchDetailsResponse;
import wtksara.plantfarm.response.PatchResponse;

import java.util.List;

public interface PatchRepository extends JpaRepository<Patch, Long> {

    @Query("SELECT new wtksara.plantfarm.response.PatchResponse(" +
            "patch.id, patch.amountOfDays, " +
            "plant.name, plant.type, plant.humidity, plant.temperature, plant.amountOfDays," +
            "measurement.humidity, measurement.temperature ) " +

            "FROM Patch patch " +
            "LEFT JOIN patch.cultivation cultivation " +
            "LEFT JOIN patch.cultivation.plant plant " +
            "LEFT JOIN patch.cultivation.measurements measurement " +
            "WHERE measurement.id NOT IN " +
            "(SELECT m1.id FROM Measurement m1, Measurement m2 WHERE m1.cultivation = m2.cultivation AND m1.date < m2.date) " +
            "OR measurement.id IS NULL " +
            "ORDER BY patch.id ASC" )

    public List<PatchResponse> getAllPatches();

    @Query("SELECT new wtksara.plantfarm.response.PatchDetailsResponse(" +
            "patch.id, patch.amountOfDays, " +
            "plant.name, plant.type, plant.humidity, plant.temperature, plant.amountOfDays)" +
            "FROM Patch patch " +
            "LEFT JOIN patch.cultivation cultivation " +
            "LEFT JOIN patch.cultivation.plant plant " +
            "ORDER BY patch.id ASC" )
    public List<PatchDetailsResponse> getAllPatchesDetails();

}


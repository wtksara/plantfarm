package wtksara.plantfarm.patch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wtksara.plantfarm.response.PatchResponse;

import java.util.List;

// Repozytorium dla encji Patch
public interface PatchRepository extends JpaRepository<Patch, Long> {

    // Utworzenie własnego zapytania do bazy
    @Query("SELECT new wtksara.plantfarm.response.PatchResponse(" +
            "patch.id, patch.amountOfDays, " +
            "plant.id, plant.name, plant.type, plant.humidity," +
            "plant.temperature, plant.amountOfDays," +
            "measurement.humidity, measurement.temperature," +
            "measurement.date) " +
            "FROM Patch patch " +
            "LEFT JOIN patch.cultivation cultivation " +
            "LEFT JOIN patch.cultivation.plant plant " +
            "LEFT JOIN patch.cultivation.measurements measurement " +
            "WHERE measurement.id NOT IN " +
            "(SELECT m1.id FROM Measurement m1, Measurement m2 " +
            "WHERE m1.cultivation = m2.cultivation " +
            "AND m1.date < m2.date)" +
            "OR measurement.id IS NULL " +
            "ORDER BY patch.id ASC" )

    // Zapytanie te zwraca wszystkie plantacje wraz z informacjami na temat rośliny,
    // która została na danej plantacji posadzona
    // oraz informacjami na temat ostatniego sporządzonego pomiaru.
    public List<PatchResponse> getAllPatches();

}


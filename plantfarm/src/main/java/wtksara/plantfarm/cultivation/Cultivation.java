package wtksara.plantfarm.cultivation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.patch.Patch;
import wtksara.plantfarm.plant.Plant;

import javax.persistence.*;
import java.util.List;

// Encja przechowujaca informacje o uprawach
@Entity
@Table(name = "cultivation")
public class Cultivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Realizacja relacji N do 1
    @ManyToOne(fetch = FetchType.EAGER)

    // Obiekt przechowujący roślinę
    @JoinColumn(name = "plant_id", referencedColumnName = "id")
    private Plant plant;

    // Realizacja relacji 1 do N
    @OneToMany(mappedBy = "cultivation", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cultivation")
    // Lista sporządzonych pomiarów dla danej uprawy
    private List<Measurement> measurements ;

    // Informacje o plantacji są ignorowane, gdyż są wykorzystywane w innej encji
    @JsonIgnore
    // Realizacja relacji 1 do 1
    @OneToOne(mappedBy = "cultivation")
    private Patch patch;

    // Zmienna przechowujaca numer plantacji na której została uprawiana dana uprawa
    @Column(name ="grow_patch")
    private Long growPatch;

    // Zmienna przechowujaca stan uprawy
    @Column(name ="finished")
    private Boolean finished;

    public Cultivation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant ) {
        this.plant = plant ;
    }

    public Long getGrowPatch() {
        return growPatch;
    }

    public void setGrowPatch(Long growPatch) {
        this.growPatch = growPatch;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}


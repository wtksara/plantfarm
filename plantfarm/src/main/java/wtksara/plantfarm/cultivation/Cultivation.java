package wtksara.plantfarm.cultivation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.patch.Patch;
import wtksara.plantfarm.plant.Plant;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cultivation")
public class Cultivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plant_id", referencedColumnName = "id")
    private Plant plant;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cultivation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("cultivation")
    private List<Measurement> measurements ;

    @JsonIgnore
    @OneToOne(mappedBy = "cultivation")
    private Patch patch ;

    @Column(name ="grow_patch")
    private Long growPatch;

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


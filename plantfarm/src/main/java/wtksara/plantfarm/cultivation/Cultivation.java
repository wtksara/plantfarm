package wtksara.plantfarm.cultivation;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne()
    @JoinColumn(name = "plant_id", referencedColumnName = "id")
    private Plant plant;

    @JsonIgnore
    @OneToMany(mappedBy = "cultivation")
    private List<Measurement> measurements ;

    @JsonIgnore
    @OneToOne(mappedBy = "cultivation")
    private Patch patch ;


    public Cultivation() {

    }


    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant ) {
        this.plant = plant ;
    }

}


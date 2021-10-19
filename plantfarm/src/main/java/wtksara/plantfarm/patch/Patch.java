package wtksara.plantfarm.patch;


import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.plant.Plant;

import javax.persistence.*;

@Entity
@Table(name = "patch")
public class Patch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "cultivation_id", referencedColumnName = "id")
    private Cultivation cultivation;

    @Column(name ="amountofdays")
    private Double amountOfDays;

    public Double getAmountOfDays() {
        return amountOfDays;
    }

    public void setAmountOfDays(Double amountOfDays) {
        this.amountOfDays = amountOfDays;
    }

    public Cultivation getCultivation() {
        return cultivation;
    }

    public void setCultivation(Cultivation cultivation) {
        this.cultivation = cultivation;
    }


}

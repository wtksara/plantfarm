package wtksara.plantfarm.patch;

import wtksara.plantfarm.cultivation.Cultivation;

import javax.persistence.*;

// Encja przechowujaca informację o plantacjach
@Entity
@Table(name = "patch")
public class Patch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "cultivation_id", referencedColumnName = "id")
    // Dana uprawa zasadzona na plantacji
    private Cultivation cultivation;

    @Column(name ="amountofdays")
    // Zmienna przechowująca liczbę dni od rozpoczecia uprawy
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

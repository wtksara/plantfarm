package wtksara.plantfarm.tank;

import javax.persistence.*;

// Encja przechowujaca informacje o zbiorniku
@Entity
@Table(name = "tank")
public class Tank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Zmienna przechowująca wartość poziomu wody
    @Column(name ="level")
    private Integer level;

    // Zmienna przechowująca poziom nasłonecznienia w danym miejscu
    @Column(name ="insolation")
    private Integer insolation;

    public Tank(){

    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getInsolation() {
        return insolation;
    }

    public void setInsolation(Integer insolation) {
        this.insolation = insolation;
    }
}

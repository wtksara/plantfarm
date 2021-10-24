package wtksara.plantfarm.tank;

import javax.persistence.*;

@Entity
@Table(name = "tank")
public class Tank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="level")
    private Integer level;

    public Tank(){

    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
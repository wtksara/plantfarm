package wtksara.plantfarm.plant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import wtksara.plantfarm.cultivation.Cultivation;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "plant")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name ="type")
    private String type;

    @Column(name ="photo")
    private String photo;

    @Column(name ="humidity")
    private Double humidity;

    @Column(name ="temperature")
    private Double temperature;

    @Column(name ="amountofdays")
    private Double amountOfDays;

    @JsonIgnore
    @OneToMany (mappedBy ="plant")
    private List<Cultivation> cultivations;

    public Plant(){

    }

    public Plant(String name, String type, String photo, Double humidity, Double temperature, Double amountOfDays) {
        this.name = name;
        this.name = type;
        this.photo = photo;
        this.humidity = humidity;
        this.temperature = temperature;
        this.amountOfDays = amountOfDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getAmountOfDays() {
        return amountOfDays;
    }

    public void setAmountOfDays(Double amountOfDays) {
        this.amountOfDays = amountOfDays;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id) &&
                Objects.equals(name, plant.name) &&
                Objects.equals(type, plant.type) &&
                Objects.equals(photo, plant.photo) &&
                Objects.equals(humidity, plant.humidity) &&
                Objects.equals(temperature, plant.temperature) &&
                Objects.equals(amountOfDays, plant.amountOfDays) &&
                Objects.equals(cultivations, plant.cultivations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, photo, humidity, temperature, amountOfDays, cultivations);
    }
}

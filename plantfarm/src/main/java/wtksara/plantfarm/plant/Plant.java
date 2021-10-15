package wtksara.plantfarm.plant;
import javax.persistence.*;


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
}


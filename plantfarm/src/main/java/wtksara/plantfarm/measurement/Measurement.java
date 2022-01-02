package wtksara.plantfarm.measurement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wtksara.plantfarm.cultivation.Cultivation;

import javax.persistence.*;
import java.util.Date;

// Encja przechowujaca sporzadzone pomiary
@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Realizacja relacji N do 1
    @ManyToOne()
    @JoinColumn(name = "cultivation_id", referencedColumnName = "id")
    @JsonIgnoreProperties("measurements")
    // Pomiar sporządzony dla danej uprawy
    private Cultivation cultivation;

    // Określenie formatu daty sporządzenia pomiaru
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name ="date")
    private Date date;

    // Zmienna przechowująca wartość pomiaru wilgotności
    @Column(name ="humidity")
    private Double humidity;

    // Zmienna przechowująca wartość pomiaru temperatury
    @Column(name ="temperature")
    private Double temperature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Cultivation getCultivation() {
        return cultivation;
    }

    public void setCultivation(Cultivation cultivation) {
        this.cultivation = cultivation;
    }

}

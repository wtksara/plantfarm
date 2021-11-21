package wtksara.plantfarm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatchResponse {

    private Long patchId;
    @Nullable
    private Double actualAmountOfDays;

    @Nullable
    private Long plantId;
    @Nullable
    private String plantName;
    @Nullable
    private String plantType;

    @Nullable
    private Double humidity;
    @Nullable
    private Double temperature;
    @Nullable
    private Double amountOfDays;


    @Nullable
    private Double actualHumidity;
    @Nullable
    private Double actualTemperature;
    @Nullable
    private Date date;


}

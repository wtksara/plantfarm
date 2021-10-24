package wtksara.plantfarm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatchDetailsResponse {
    private Long patchId;
    @Nullable
    private Double actualAmountOfDays;

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

}

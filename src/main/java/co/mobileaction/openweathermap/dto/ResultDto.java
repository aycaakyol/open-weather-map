package co.mobileaction.openweathermap.dto;

import co.mobileaction.openweathermap.model.PollutionHistory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDto
{
    private String city;
    private List<PollutionHistory> results;
}

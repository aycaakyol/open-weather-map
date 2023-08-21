package co.mobileaction.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollutionHistoryDto
{
    private Long id;
    private String city;
    private LocalDate day;
    private String co;
    private String so2;
    private String o3;
}

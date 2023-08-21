package co.mobileaction.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollutionValuesByDtDto
{
    private PollutionValuesDto components;   // these names have to be components and dt because of external api call
    private Long dt;
}


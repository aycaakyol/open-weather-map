package co.mobileaction.openweathermap.service;

import co.mobileaction.openweathermap.dto.ResultDto;
import co.mobileaction.openweathermap.model.PollutionHistory;

import java.time.LocalDate;

public interface IPollutionService
{
    void savePollution(PollutionHistory pollution);

    ResultDto getPollutionCategories(String cityName, LocalDate startDate, LocalDate endDate);
}

package co.mobileaction.openweathermap.service;

import co.mobileaction.openweathermap.dto.ResultDto;
import co.mobileaction.openweathermap.dto.ExternalApiCallResultDto;
import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.model.PollutionHistory;
import co.mobileaction.openweathermap.redis.RedisUtility;
import co.mobileaction.openweathermap.repository.IPollutionRepository;
import co.mobileaction.openweathermap.utils.PollutionUtils;
import co.mobileaction.openweathermap.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PollutionService implements IPollutionService
{
    private final IPollutionRepository pollutionRepository;
    private final CityService cityService;

    @Autowired
    private final RedisUtility redisUtility;

    private final String POLLUTION_URL = "http://api.openweathermap.org/data/2.5/air_pollution/history?lat=%f&lon=%f&start=%d&end=%d&appid=%s";

    @Override
    public void savePollution(PollutionHistory pollution)
    {
        pollutionRepository.save(pollution);
    }

    // this function gives pollution values -- multiple values by day,
    private ExternalApiCallResultDto externalApiCall(float lat, float lon, long start, long end, String appid)
    {
        RestTemplate restTemplate = new RestTemplate();

        String url = String.format(POLLUTION_URL, lat, lon, start, end, appid);

        ExternalApiCallResultDto responseDto = restTemplate.getForObject(url, ExternalApiCallResultDto.class);

        return responseDto;
    }

    // This function makes the external api call and creates new PollutionHistory objects and returns them
    // start is the first day and end is the last day
    private ArrayList<PollutionHistory> getPollutionFromExternalApiCall(String cityName, LocalDate start, LocalDate end)
    {
        long startUnixTime = start.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();
        long endUnixTime = end.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond() + 86400L - 1L;

        City city = cityService.getAndSaveCity(cityName, SecurityUtils.API_KEY);
        float lat = city.getLat();
        float lon = city.getLon();

        ExternalApiCallResultDto dto = externalApiCall(lat, lon, startUnixTime, endUnixTime, SecurityUtils.API_KEY);

        return PollutionUtils.calculatePollutionByDay(dto, start, city);
    }

    // this function makes query for every date between start and end dates, take data from database or
    // make external api call and save to the database later on. Also logs.
    @Override
    public ResultDto getPollutionCategories(String cityName, LocalDate startDate, LocalDate endDate)
    {
        saveLastWeekToRedis(cityName);
        if(startDate == null)
        {
            endDate = LocalDate.now();
            startDate = endDate.minusWeeks(1);
        }

        List<LocalDate> allDates = startDate.datesUntil(endDate).collect(Collectors.toList());
        allDates.add(endDate);

        ArrayList<PollutionHistory> allPollutions = new ArrayList<>();

        int tracker = 0;

        String logInfo = "Query: %s, %s and %s ->";
        String formattedLogInfo = String.format(logInfo, cityName, startDate, endDate);
        log.info(formattedLogInfo);

        for(int i = 0; i < allDates.size() - 1; i++)
        {
            PollutionHistory todayPollution = pollutionRepository.findByDay(cityName, allDates.get(i));
            PollutionHistory tomorrowPollution = pollutionRepository.findByDay(cityName, allDates.get(i+1));

            if(todayPollution != null)
            {
                allPollutions.add(todayPollution);
//                redisUtility.setPollutionHistoryValue(Long.toString(todayPollution.getId()), todayPollution);
            }

            if(todayPollution != null && tomorrowPollution == null)
            {
                logInfo = "%s and %s will be taken from the database";
                formattedLogInfo = String.format(logInfo, allDates.get(tracker), allDates.get(i));
                log.info(formattedLogInfo);

                tracker = i + 1;
            }
            else if ((todayPollution == null && tomorrowPollution != null) || (i == allDates.size() - 2 && todayPollution == null))
            {
                LocalDate start = allDates.get(tracker);
                LocalDate end = (i == allDates.size() - 2) ? allDates.get(i+1) : allDates.get(i);

                ArrayList<PollutionHistory> newPollutions = getPollutionFromExternalApiCall(cityName, start, end);
                allPollutions.addAll(newPollutions);

                logInfo = "%s and %s will be taken from the API and saved to the database";
                formattedLogInfo = String.format(logInfo, start, end);
                log.info(formattedLogInfo);

                tracker = i + 1;
            }
        }

        if(allPollutions.size() != allDates.size())
        {
            LocalDate start = allDates.get(tracker);
            LocalDate end = allDates.get(allDates.size() - 1);

            ArrayList<PollutionHistory> newPollutions = getPollutionFromExternalApiCall(cityName, start, end);
            allPollutions.addAll(newPollutions);

            logInfo = "%s  will be taken from the API and saved to the database";
            formattedLogInfo = String.format(logInfo, start);
            log.info(formattedLogInfo);
        }

        var resultDto = new ResultDto();
        resultDto.setCity(cityName);
        resultDto.setResults(allPollutions);

        pollutionRepository.saveAll(allPollutions);

        return resultDto;
    }

    // this function saves the last weeks data to redis
    private void saveLastWeekToRedis(String cityName)
    {
        LocalDate weekEnd = LocalDate.now();
        LocalDate weekStart = weekEnd.minusWeeks(1);

        String key = cityName + "-%s";

        ArrayList<PollutionHistory> newPollutions = getPollutionFromExternalApiCall(cityName, weekStart, weekEnd);
        newPollutions.forEach(pollution -> redisUtility.setPollutionHistoryValue(String.format(key, pollution.getDay()), pollution));

    }
}
package co.mobileaction.openweathermap.controller;

import co.mobileaction.openweathermap.dto.ResultDto;
import co.mobileaction.openweathermap.exception.QueryOutOfRangeException;
import co.mobileaction.openweathermap.redis.RedisUtility;
import co.mobileaction.openweathermap.service.IPollutionService;
import co.mobileaction.openweathermap.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@Secured(SecurityUtils.ROLE_USER)
@RequestMapping("api/pollution")
@RequiredArgsConstructor
public class PollutionController
{
    private final IPollutionService service;

    @GetMapping("/result")  // this function will call the other two and give the end result
    public ResponseEntity<ResultDto> getPollutionCategories(@RequestParam(value = "city") String city,
                                                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate,
                                                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate endDate)
    {
        LocalDate firstDate = LocalDate.of(2020, 11, 27);
        LocalDate now = LocalDate.now();

        if(startDate.isBefore(firstDate) || endDate.isAfter(now))
        {
            throw new QueryOutOfRangeException("Data does not exist for these dates");
        }

        return ResponseEntity.ok(service.getPollutionCategories(city, startDate, endDate));
    }
}

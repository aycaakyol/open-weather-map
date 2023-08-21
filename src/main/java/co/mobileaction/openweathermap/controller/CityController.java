package co.mobileaction.openweathermap.controller;

import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.service.ICityService;
import co.mobileaction.openweathermap.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@Secured(SecurityUtils.ROLE_USER)
@RequestMapping("api/city")
@RequiredArgsConstructor
public class CityController
{
    private final ICityService service;

    @GetMapping
    public ResponseEntity<City> getAndSaveCity(@RequestParam("cityName") String cityName, @RequestParam("appid") String appid)
    {
        City city = service.getAndSaveCity(cityName, appid);
        return ResponseEntity.ok(city);
    }
}

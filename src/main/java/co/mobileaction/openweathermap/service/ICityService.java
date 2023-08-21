package co.mobileaction.openweathermap.service;

import co.mobileaction.openweathermap.model.City;

public interface ICityService
{
    void saveCity(City city);

    City getAndSaveCity(String cityName, String appid);
}

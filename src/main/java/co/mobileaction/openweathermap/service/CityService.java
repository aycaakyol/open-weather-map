package co.mobileaction.openweathermap.service;

import co.mobileaction.openweathermap.dto.CityDto;
import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.repository.ICityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class CityService implements ICityService  //todo do this with redis
{
    private final ICityRepository cityRepository;

    private final ModelMapper mapper;

    private final String CITY_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";
    @Override
    public void saveCity(City city)
    {
        cityRepository.save(city);
    }

    @Override
    public City getAndSaveCity(String cityName, String appid)
    {
        CityDto dto = externalApiCall(cityName, appid);

        City city = convertToModel(dto);

        cityRepository.save(city);

        return cityRepository.findCityByName(cityName);
    }

    private CityDto externalApiCall(String cityName, String appid)
    {
        RestTemplate restTemplate = new RestTemplate();

        String url = String.format(CITY_URL, cityName, appid);

        CityDto[] cityDtos = restTemplate.getForObject(url, CityDto[].class);

        return cityDtos[0];
    }

    private City convertToModel(CityDto dto)
    {
        City city = mapper.map(dto, City.class);
        return city;
    }
}

package co.mobileaction.openweathermap.service;

import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.repository.ICityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests
{
    @Mock
    private ICityRepository cityRepository;

    @Mock
    private ModelMapper modelMapper;

    private CityService cityService;

    @BeforeEach
    public void setUp()
    {
        cityService = new CityService(cityRepository, modelMapper);
    }

    @Test
    public void saveCity()
    {
        City city = new City("Ankara", 50.50f, 50.50f);

        when(cityRepository.save(city)).thenReturn();
    }

    @Test
    public void getAndSaveCity()
    {
        when(cityRepository.findCityByName()).thenReturn()
    }
}

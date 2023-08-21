package co.mobileaction.openweathermap.repository;

import co.mobileaction.openweathermap.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICityRepository extends JpaRepository<City, Long>
{
    City findCityByName(String name);
}

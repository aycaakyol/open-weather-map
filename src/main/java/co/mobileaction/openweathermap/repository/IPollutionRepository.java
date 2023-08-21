package co.mobileaction.openweathermap.repository;


import co.mobileaction.openweathermap.model.PollutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface IPollutionRepository extends JpaRepository<PollutionHistory, Long>
{
    @Query(value = "SELECT * FROM pollution WHERE city = :city AND day = :day", nativeQuery = true)
    PollutionHistory findByDay(@Param("city") String city, @Param("day") LocalDate day);
}

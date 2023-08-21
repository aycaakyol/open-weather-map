package co.mobileaction.openweathermap;

import co.mobileaction.openweathermap.model.PollutionHistory;
import co.mobileaction.openweathermap.service.PollutionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
//@ComponentScan("co.mobileaction.openweathermap")
public class OpenweathermapApplication {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(OpenweathermapApplication.class, args);
	}

}

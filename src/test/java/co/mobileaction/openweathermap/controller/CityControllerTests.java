package co.mobileaction.openweathermap.controller;

import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.service.ICityService;
import co.mobileaction.openweathermap.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CityController.class)
@ContextConfiguration(classes = CityController.class)
@WithMockUser(roles = {SecurityUtils.USER})
public class CityControllerTests extends ControllerTestsBase
{
    @MockBean
    private ICityService cityService;

    private final String CITY_NAME = "Ankara";

    @Test
    public void getAndSaveCity() throws Exception
    {
        when(cityService.getAndSaveCity(CITY_NAME, SecurityUtils.API_KEY)).thenReturn(new City());

        this.mockMvc.perform(get("/api/city")
                    .param("cityName", CITY_NAME)
                    .param("appid", SecurityUtils.API_KEY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty());

        verify(cityService).getAndSaveCity(CITY_NAME, SecurityUtils.API_KEY);
    }
}

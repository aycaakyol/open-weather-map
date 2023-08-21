package co.mobileaction.openweathermap.controller;

import co.mobileaction.openweathermap.dto.ResultDto;
import co.mobileaction.openweathermap.exception.QueryOutOfRangeException;
import co.mobileaction.openweathermap.service.IPollutionService;
import co.mobileaction.openweathermap.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PollutionController.class)
@ContextConfiguration(classes = PollutionController.class)
@WithMockUser(roles = {SecurityUtils.USER})
public class PollutionControllerTests extends ControllerTestsBase
{
    @MockBean
    private IPollutionService pollutionService;

    private final String CITY_NAME = "Ankara";
    private final String START_DATE_STR = "16-05-2021";
    private final String END_DATE_STR = "18-05-2021";
    private final String WRONG_START_DATE_STR = "12-10-2019";
    private final LocalDate START_DATE = LocalDate.of(2021,5,16);
    private final LocalDate END_DATE = LocalDate.of(2021,5,18);
    private final LocalDate WRONG_START_DATE = LocalDate.of(2019,10,12);

    @Test
    public void getPollutionCategories() throws Exception
    {
        when(pollutionService.getPollutionCategories(CITY_NAME, START_DATE, END_DATE)).thenReturn(new ResultDto());

        this.mockMvc.perform(get("/api/pollution/result")
                        .param("city", CITY_NAME)
                        .param("startDate", START_DATE_STR)
                        .param("endDate", END_DATE_STR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(pollutionService).getPollutionCategories(CITY_NAME, START_DATE, END_DATE);
    }

    // why is this not passing AAAAAAAAAAAAAAAAAAAAAAAAAAA
    @Test
    public void getPollutionCategories_getException() throws Exception
    {
//        when(pollutionService.getPollutionCategories(CITY_NAME, WRONG_START_DATE, END_DATE)).thenReturn(new ResultDto());

        this.mockMvc.perform(get("/api/pollution/result")
                        .param("city", CITY_NAME)
                        .param("startDate", WRONG_START_DATE_STR)
                        .param("endDate", END_DATE_STR))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof QueryOutOfRangeException))
                .andExpect(result -> assertEquals("Data does not exist for these dates", result.getResolvedException().getMessage()));
    }
}

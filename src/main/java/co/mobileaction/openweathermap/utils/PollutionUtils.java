package co.mobileaction.openweathermap.utils;

import co.mobileaction.openweathermap.dto.ExternalApiCallResultDto;
import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.model.PollutionHistory;

import java.time.LocalDate;
import java.util.ArrayList;

public class PollutionUtils
{
    public static float round(float value)
    {
        float v = Math.round(value * 10) / 10;
        return v;
    }

    //switch case cannot be used here because we're doing comparisions
    public static String categorizeCo(float coValue)
    {
        coValue = round(coValue);

        if(coValue <= 1.1)
        {
            return "Good";
        }
        else if (coValue <= 2.0)
        {
            return "Satisfactory";
        }
        else if (coValue <= 10.0)
        {
            return "Moderate";
        }
        else if (coValue <= 17.0)
        {
            return "Poor";
        }
        else if (coValue <= 34.0)
        {
            return "Severe";
        }
        else
        {
            return "Hazardous";
        }
    }

    public static String categorizeO3(float o3Value)
    {
        o3Value = round(o3Value);

        if(o3Value <= 50.9)
        {
            return "Good";
        }
        else if (o3Value <= 100.9)
        {
            return "Satisfactory";
        }
        else if (o3Value <= 168.9)
        {
            return "Moderate";
        }
        else if (o3Value <= 208.9)
        {
            return "Poor";
        }
        else if (o3Value <= 748.0)
        {
            return "Severe";
        }
        else
        {
            return "Hazardous";
        }

    }

    public static String categorizeSo2(float so2Value)
    {
        so2Value = round(so2Value);

        if(so2Value <= 40.9)
        {
            return "Good";
        }
        else if (so2Value <= 80.9)
        {
            return "Satisfactory";
        }
        else if (so2Value <= 380.9)
        {
            return "Moderate";
        }
        else if (so2Value <= 800.9)
        {
            return "Poor";
        }
        else if (so2Value <= 1600.0)
        {
            return "Severe";
        }
        else
        {
            return "Hazardous";
        }
    }

    // this function goes through all the items from the dto and calculates average value by day
    public static ArrayList<PollutionHistory> calculatePollutionByDay(ExternalApiCallResultDto dto, LocalDate startDate, City city)
    {
        var list = dto.getList();
        float co_sum = 0;
        float o3_sum = 0;
        float so2_sum = 0;
        int  count = 0;
        long start = list.get(0).getDt();
        long idNum = 0;
        ArrayList<PollutionHistory> newPollutionsList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getDt() - start >= 86400L)
            {
                PollutionHistory newPollution = new PollutionHistory();
                newPollution.setId(idNum);
                newPollution.setCity(city);
                newPollution.setDay(startDate);
                newPollution.setCo(PollutionUtils.categorizeCo(co_sum / count));
                newPollution.setO3(PollutionUtils.categorizeO3(o3_sum / count));
                newPollution.setSo2(PollutionUtils.categorizeSo2(so2_sum / count));
                newPollutionsList.add(newPollution);

                startDate = startDate.plusDays(1);
                co_sum = 0;
                o3_sum = 0;
                so2_sum = 0;
                count = 0;
                start += 86400L;
                idNum++;
            }
            else
            {
                co_sum += list.get(i).getComponents().getCo();
                o3_sum += list.get(i).getComponents().getO3();
                so2_sum += list.get(i).getComponents().getSo2();
                count++;
            }
        }

        if (count > 0) {
            PollutionHistory newPollution = new PollutionHistory();
            newPollution.setId(idNum);
            newPollution.setCity(city);
            newPollution.setDay(startDate);
            newPollution.setCo(PollutionUtils.categorizeCo(co_sum / count));
            newPollution.setO3(PollutionUtils.categorizeO3(o3_sum / count));
            newPollution.setSo2(PollutionUtils.categorizeSo2(so2_sum / count));
            newPollutionsList.add(newPollution);
        }

        return newPollutionsList;
    }
}

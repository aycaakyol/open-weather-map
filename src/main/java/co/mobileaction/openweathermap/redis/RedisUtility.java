package co.mobileaction.openweathermap.redis;

import co.mobileaction.openweathermap.model.City;
import co.mobileaction.openweathermap.model.PollutionHistory;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class RedisUtility
{
    @Qualifier("CityRedisTemplate")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    Gson gson;

    public void setCityValue(final String key, City city)
    {
        redisTemplate.opsForValue().set(key, gson.toJson(city));
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    public void setPollutionHistoryValue(final String key, PollutionHistory pollutionHistory)
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(new TypeToken<LocalDate>(){}.getType(), new LocalDateConverter());
        Gson gson1 = builder.create();

        redisTemplate.opsForValue().set(key, gson1.toJson(pollutionHistory));
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    public static class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate>
    {
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE.format(src));
        }

        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return DateTimeFormatter.ISO_LOCAL_DATE.parse(json.getAsString(), LocalDate::from);
        }
    }

    public String getValue(final String key)
    {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKeyFromRedis(String key)
    {
        redisTemplate.delete(key);
    }
}

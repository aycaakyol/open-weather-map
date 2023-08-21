package co.mobileaction.openweathermap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@RedisHash("City")
public class City implements Serializable
{

    @Id
    private String name;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;
}

package co.mobileaction.openweathermap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pollution")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollutionHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    private City city;

    @Column(name = "day")
    private LocalDate day;

    @Column(name = "co")
    private String co;

    @Column(name = "so2")
    private String so2;

    @Column(name = "o3")
    private String o3;
}

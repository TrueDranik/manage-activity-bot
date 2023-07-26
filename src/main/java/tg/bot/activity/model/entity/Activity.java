package tg.bot.activity.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String seasonality;
    @OneToOne
    @JoinColumn(name = "activity_format_id")
    private ActivityFormat activityFormat;
    @OneToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;
    @Column(length = 3000)
    private String description;
    private String duration;
    @Column(length = 5)
    private String age;
    @Column(length = 30)
    private String complexity;
    private BigDecimal price;
    private Boolean isActive;
    private int prepayPercent;
}

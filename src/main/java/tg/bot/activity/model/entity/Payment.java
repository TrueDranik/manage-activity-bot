package tg.bot.activity.model.entity;

import com.bot.sup.common.enums.PaymentTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private PaymentTypeEnum type;
    private String description;
    private String data;
}

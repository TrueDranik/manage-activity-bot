package tg.bot.activity.model.entity;

import lombok.Getter;
import lombok.Setter;
import tg.bot.activity.common.enums.PaymentTypeEnum;

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

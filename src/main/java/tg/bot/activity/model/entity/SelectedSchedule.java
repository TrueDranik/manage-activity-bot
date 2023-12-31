package tg.bot.activity.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
public class SelectedSchedule {

    @Id
    private Long telegramId;
    private Long currentScheduleId;
}

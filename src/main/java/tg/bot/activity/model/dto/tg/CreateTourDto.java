package tg.bot.activity.model.dto.tg;

import lombok.Data;

@Data
public class CreateTourDto {

    private RouteDto route;
    private ScheduleDto scheduleDto;
    private ActivityDto activityDto;
}

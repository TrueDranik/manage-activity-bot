package tg.bot.activity.model;

import lombok.Data;

@Data
public class ActivityRequestParams {

    private Long activityFormatId;
    private Long activityTypeId;
    private Long routeId;
}

package tg.bot.activity.common.enums.states;

public enum ActivityFormatStateEnum implements StateEnum<ActivityFormatStateEnum> {

    FILLING_ACTIVITY_FORMAT,
    START_PROCESSING,
    ASK_ACTIVITY_FORMAT_NAME,
    ASK_ACTIVITY_FORMAT_DESCRIPTION,
    ASK_ACTIVITY_FORMAT_PHOTO;

    @Override
    public ActivityFormatStateEnum[] getValues() {
        return ActivityFormatStateEnum.values();
    }
}

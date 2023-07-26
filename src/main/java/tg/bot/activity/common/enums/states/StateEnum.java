package tg.bot.activity.common.enums.states;

public interface StateEnum<T extends Enum<T>>{

    T[] getValues();
}

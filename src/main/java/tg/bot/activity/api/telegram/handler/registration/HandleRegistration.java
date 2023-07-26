package tg.bot.activity.api.telegram.handler.registration;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.common.enums.states.StateEnum;

public interface HandleRegistration {

    BotApiMethod<?> resolveState(Message message);

    StateEnum<?> getType();
}

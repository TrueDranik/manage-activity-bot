package tg.bot.activity.api.telegram.handler.registration.activity.format.states;

import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;

public interface ActivityFormatMessageProcessor extends MessageProcessor {

    ActivityFormatStateEnum getCurrentState();
}

package tg.bot.activity.api.telegram.handler.registration.activity.type.states;

import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.common.enums.states.ActivityTypeStateEnum;

public interface ActivityTypeMessageProcessor extends MessageProcessor {

    ActivityTypeStateEnum getCurrentState();
}

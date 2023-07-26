package tg.bot.activity.api.telegram.handler.registration.information.states;

import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.common.enums.states.InformationAboutUsStateEnum;

public interface InformationAboutUsMessageProcessor extends MessageProcessor {
    InformationAboutUsStateEnum getCurrentState();
}

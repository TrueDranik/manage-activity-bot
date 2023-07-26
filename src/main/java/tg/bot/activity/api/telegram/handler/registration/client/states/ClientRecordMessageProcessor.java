package tg.bot.activity.api.telegram.handler.registration.client.states;

import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;

public interface ClientRecordMessageProcessor extends MessageProcessor {

    ClientRecordStateEnum getCurrentState();
}

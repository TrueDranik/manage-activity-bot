package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.common.enums.states.InstructorStateEnum;

public interface InstructorMessageProcessor extends MessageProcessor {

    InstructorStateEnum getCurrentState();
}

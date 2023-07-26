package tg.bot.activity.api.telegram.handler.registration.instructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.api.telegram.handler.registration.instructor.states.InstructorMessageProcessor;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.util.MessageProcessorUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingInstructor implements InstructorHandleRegistration {

    private final UserStateCache userStateCache;
    private final Map<InstructorStateEnum, InstructorMessageProcessor> instructorMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByChatId(chatId);
        Instructor instructor = (Instructor) userState.getEntity();

        if (userState.getState().equals(InstructorStateEnum.FILLING_INSTRUCTOR)) {
            userState.setState(InstructorStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        InstructorStateEnum instructorCurrentState = (InstructorStateEnum) userStateCache.getByChatId(chatId).getState();
        MessageProcessor messageProcessor = instructorMessageProcessorMap.get(instructorCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, instructor);
    }

    @Override
    public InstructorStateEnum getType() {
        return InstructorStateEnum.FILLING_INSTRUCTOR;
    }
}

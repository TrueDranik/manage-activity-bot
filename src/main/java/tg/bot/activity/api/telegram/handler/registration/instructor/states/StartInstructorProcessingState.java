package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.service.MessageService;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartInstructorProcessingState implements InstructorMessageProcessor {

    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();

        log.info("start instructor registration");

        UserState userState = userStateCache.getByChatId(chatId);
        userState.setState(InstructorStateEnum.ASK_FULL_NAME);
        userStateCache.createOrUpdateState(userState);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructor());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return null;
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return false;
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.START_PROCESSING;
    }
}

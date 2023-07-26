package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.validation.Validation;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskInstructorPhoneNumberState implements InstructorMessageProcessor {

    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        ((Instructor) instructor).setPhoneNumber(userAnswer);

        log.info("instructor phone number = " + userAnswer);

        userStateCache.getByChatId(chatId).setState(InstructorStateEnum.ASK_PHOTO);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getGetPhoto());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getPhoneNumberNotValid());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return !Validation.isValidPhoneNumber(message.getText());
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_PHONE_NUMBER;
    }
}

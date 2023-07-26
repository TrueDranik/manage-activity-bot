package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.validation.Validation;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskInstructorFullNameState implements InstructorMessageProcessor {

    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;

    private String errorMessage = "";

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();
        String[] fullName = userAnswer.split(" ");

        UserState cacheByTelegramId = userStateCache.getByChatId(chatId);

        ((Instructor) instructor).setFirstName(fullName[0]);
        ((Instructor) instructor).setLastName(fullName[1]);

        log.info("instructor Name = " + userAnswer);

        cacheByTelegramId.setState(InstructorStateEnum.ASK_PHONE_NUMBER);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputPhoneNumber());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        userStateCache.getByChatId(chatId).setState(InstructorStateEnum.ASK_FULL_NAME);

        return messageService.buildReplyMessage(chatId, errorMessage + "\nВведите ФИ заново!");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String messageText = message.getText();
        String[] split = messageText.split(" ");
        if (split.length < 2) {
            return true;
        }
        boolean invalidFirstName = split[0].length() < 2 || split[0].length() > 15;
        boolean invalidLastName = split[1].length() < 2 || split[1].length() > 15;

        boolean validText = !Validation.isValidText(messageText);
        boolean validFio = !StringUtils.hasText(messageText) || invalidFirstName || invalidLastName;
        if (validText) {
            errorMessage = instructorMessageProperties.getValidateLanguage();
        } else if (validFio) {
            errorMessage = instructorMessageProperties.getValidateInputFullName();
        }

        return validText || validFio;
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_FULL_NAME;
    }
}

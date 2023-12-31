package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.instructor.InstructorService;
import tg.bot.activity.util.KeyboardUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskInstructorTelegramIdState implements InstructorMessageProcessor {

    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorService instructorService;
    private final UserStateCache userStateCache;

    private String errorMessage = "";

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();

        ((Instructor) instructor).setTelegramId(message.getForwardFrom().getId());
        if (message.getForwardFrom().getUserName() != null) {
            ((Instructor) instructor).setUsername(message.getForwardFrom().getUserName());
        } else {
            ((Instructor) instructor).setUsername("");
        }

        log.info("instructor TelegramId = " + message.getForwardFrom().getId());
        log.info("User name = " + message.getForwardFrom().getUserName());

        instructorService.save((Instructor) instructor);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .inlineKeyboardMarkupWithOneButton(CallbackEnum.INSTRUCTORS.toString(), instructorMessageProperties.getMenuInstructors());

        return messageService.getReplyMessageWithKeyboard(chatId, instructorMessageProperties.getRegistrationDone() +
                instructorInfo(((Instructor) instructor)), keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, errorMessage + "\nПовторите попытку!");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        User dataForwardFromMessage = message.getForwardFrom();

        boolean hasDataInMessage = dataForwardFromMessage != null;
        if (hasDataInMessage) {
            UserState userCacheByTelegramId = userStateCache.getByChatId(message.getChatId());
            Instructor instructor = (Instructor) userCacheByTelegramId.getEntity();
            Long instructorTelegramId = instructor.getTelegramId();

            boolean isInstructorForUpdate = userCacheByTelegramId.isForUpdate();
            boolean isInstructorExistsByMessageData = existsByTelegramId(dataForwardFromMessage.getId());
            boolean isExistsUpdatableInstructor = existsByTelegramId(instructorTelegramId);
            if (isInstructorForUpdate) {
                boolean isInstructorExistsInDb = isExistsUpdatableInstructor || isInstructorExistsByMessageData;
                if (isInstructorExistsInDb) {
                    errorMessage = instructorMessageProperties.getTelegramIdAlreadyTaken();
                }

                return isInstructorExistsInDb;
            } else {
                if (isInstructorExistsByMessageData) {
                    errorMessage = instructorMessageProperties.getTelegramIdAlreadyTaken();
                }

                return isInstructorExistsByMessageData;
            }
        } else {
            errorMessage = "Сообщение не содержит необходимых данных! Возможно, пользователь отсутствует в контактах Телеграм.";

            return true;
        }
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_TELEGRAM_ID;
    }

    private boolean existsByTelegramId(Long telegramId) {
        return instructorService.existsByTelegramId(telegramId);
    }

    private String instructorInfo(Instructor instructor) {
        return "ФИ: " + instructor.getFirstName() + " " + instructor.getLastName()
                + "\nНомер телефона: " + instructor.getPhoneNumber()
                + "\nИмя пользователя: @" + instructor.getUsername();
    }
}

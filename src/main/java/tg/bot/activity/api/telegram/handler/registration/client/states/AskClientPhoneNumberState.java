package tg.bot.activity.api.telegram.handler.registration.client.states;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.validation.Validation;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskClientPhoneNumberState implements ClientRecordMessageProcessor {

    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        ((Client) client).setPhoneNumber(userAnswer);

        log.info("instructor phone number = " + userAnswer);

        userStateCache.getByChatId(chatId).setState(ClientRecordStateEnum.ASK_BIRTHDAY);

        return messageService.buildReplyMessage(chatId, "Введите дату рождения в формате дд.мм.гггг");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, "Неверный формат номера");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return !Validation.isValidPhoneNumber(message.getText());
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_PHONE_NUMBER;
    }
}

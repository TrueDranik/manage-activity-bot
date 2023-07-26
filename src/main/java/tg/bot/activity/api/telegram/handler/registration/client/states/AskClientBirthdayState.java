package tg.bot.activity.api.telegram.handler.registration.client.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.client.ClientServiceImpl;
import tg.bot.activity.util.KeyboardUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AskClientBirthdayState implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final ClientServiceImpl clientService;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.from(formatter.parse(userAnswer));

        ((Client) client).setBirthDate(birthday);

        clientService.save((Client) client);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .inlineKeyboardMarkupWithOneButton(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString(), "Зарегистрировано");

        return messageService.getReplyMessageWithKeyboard(chatId, "Клиент записан!", keyboardMarkup);
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
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_BIRTHDAY;
    }
}

package tg.bot.activity.api.telegram.handler.registration.client.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.repository.ClientRepository;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.client.ClientServiceImpl;
import tg.bot.activity.util.KeyboardUtil;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AskClientTelegramIdState implements ClientRecordMessageProcessor {

    private final MessageService messageService;
    private final ClientRepository clientRepository;
    private final ClientServiceImpl clientService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);

//        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());

//        Optional<Client> clientByTelegramId = clientService.findByTelegramId(forwardFrom.get().getId());

//        Client clientChoice = clientByTelegramId.get();

        ((Client) client).setTelegramId(message.getForwardFrom().getId());
        ((Client) client).setUsername(message.getForwardFrom().getUserName());

        userState.setState(ClientRecordStateEnum.ASK_FULL_NAME);

        return messageService.buildReplyMessage(chatId, "Введите ФИ клиента");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
//        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
//        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(forwardFrom.get().getId());
        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .inlineKeyboardMarkupWithOneButton(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString(), "Зарегистрировано");

        return messageService.getReplyMessageWithKeyboard(chatId, "Найден клиент: " /*+ clientByTelegramId.get().getFirstName()
                + " " + clientByTelegramId.get().getLastName()*/, keyboardMarkup);
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        Long telegramId = forwardFrom.get().getId();
        Optional<Client> clientByTelegramId = clientRepository.findByTelegramId(telegramId);

        return clientRepository.existsByTelegramId(telegramId) && clientByTelegramId.isPresent();
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.ASK_TELEGRAM_ID;
    }
}

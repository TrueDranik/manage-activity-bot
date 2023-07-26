package tg.bot.activity.api.telegram.handler.registration.client.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.service.MessageService;

@Component
@RequiredArgsConstructor
public class StartClientProcessorRecordState implements ClientRecordMessageProcessor {

    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);
        userState.setState(ClientRecordStateEnum.ASK_TELEGRAM_ID);

        return messageService.buildReplyMessage(chatId, "Перешлите любое сообщение клиента");
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
        return ClientRecordStateEnum.START_PROCESSING;
    }
}

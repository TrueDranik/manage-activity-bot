package tg.bot.activity.api.telegram.handler.registration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.api.telegram.handler.registration.client.states.ClientRecordMessageProcessor;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.common.enums.states.StateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.util.MessageProcessorUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartFillingClientImpl implements ClientHandleRegistration {

    private final UserStateCache userStateCache;
    private final Map<ClientRecordStateEnum, ClientRecordMessageProcessor> clientRecordMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByChatId(chatId);
        Client client = (Client) userState.getEntity();

        if (userState.getState().equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            userState.setState(ClientRecordStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        ClientRecordStateEnum clientRecordCurrentState = (ClientRecordStateEnum) userStateCache.getByChatId(chatId).getState();
        MessageProcessor messageProcessor = clientRecordMessageProcessorMap.get(clientRecordCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, client);
    }

    @Override
    public StateEnum<?> getType() {
        return ClientRecordStateEnum.FILLING_CLIENT;
    }
}

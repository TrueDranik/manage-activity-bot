package tg.bot.activity.service.callbackquery.impl.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.api.telegram.handler.registration.StateContext;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ClientRecordStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.service.callbackquery.Callback;

import static tg.bot.activity.common.enums.CallbackEnum.CLIENT_RECORD;

@Service
@RequiredArgsConstructor
public class CallbackClientRecordImpl implements Callback {

    private final StateContext stateContext;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        ClientRecordStateEnum botStateEnum = ClientRecordStateEnum.FILLING_CLIENT;
        Client client = new Client();

        UserState userState = new UserState();
        userState.setAdminChatId(chatId);
        userState.setState(botStateEnum);
        userState.setEntity(client);
        userState.setForUpdate(false);

        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CLIENT_RECORD;
    }
}

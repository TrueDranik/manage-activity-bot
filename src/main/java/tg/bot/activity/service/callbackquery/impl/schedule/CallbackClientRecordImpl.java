package tg.bot.activity.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.registration.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Client;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.bot.sup.common.enums.CallbackEnum.CLIENT_RECORD;

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

package tg.bot.activity.service.callbackquery.impl.activity.format;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.api.telegram.handler.registration.StateContext;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.util.UserStateUtil;

@Service
@RequiredArgsConstructor
public class CallbackActivityFormatChangeDataImpl implements Callback {

    private final StateContext stateContext;
    private final ActivityFormatServiceImpl activityFormatService;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        ActivityFormatStateEnum activityFormatStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;

        ActivityFormat activityFormat = activityFormatService.findActivityFormatById(Long.valueOf(activityFormatId));

        UserState userState = UserStateUtil.getUserState(chatId, activityFormatStateEnum, activityFormat, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(activityFormatStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITY_FORMAT_CHANGE;
    }
}

package tg.bot.activity.service.callbackquery.impl.activity.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.api.telegram.handler.registration.StateContext;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ActivityTypeStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.service.activity.type.impl.ActivityTypeServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.util.UserStateUtil;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeChangeDataImpl implements Callback {

    private final StateContext stateContext;
    private final ActivityTypeServiceImpl activityTypeService;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityTypeId = callbackQuery.getData().split("/")[1];
        ActivityTypeStateEnum activityTypeStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;

        ActivityType activityType = activityTypeService.findActivityTypeById(Long.valueOf(activityTypeId));

        UserState userState = UserStateUtil.getUserState(chatId, activityTypeStateEnum, activityType, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(activityTypeStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITY_TYPE_CHANGE;
    }
}

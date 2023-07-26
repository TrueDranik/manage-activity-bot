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
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.repository.ActivityFormatRepository;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.util.UserStateUtil;

import javax.persistence.EntityNotFoundException;

import static tg.bot.activity.common.enums.CallbackEnum.ADD_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackChooseActivityFormatToActivityTypeImpl implements Callback {

    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final ActivityFormatRepository activityFormatRepository;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        Long formatId = Long.valueOf(callbackQuery.getData().split("/")[1]);
        ActivityTypeStateEnum botStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
        ActivityFormat activityFormat = activityFormatRepository.findById(formatId)
                .orElseThrow(() -> new EntityNotFoundException("Activity format with id[" + formatId + "] not found"));
        ActivityType activityType = new ActivityType();
        activityType.setActivityFormat(activityFormat);

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, activityType, false);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ADD_ACTIVITY_TYPE;
    }
}

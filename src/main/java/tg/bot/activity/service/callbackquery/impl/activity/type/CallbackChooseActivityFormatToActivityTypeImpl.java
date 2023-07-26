package tg.bot.activity.service.callbackquery.impl.activity.type;

import com.bot.sup.api.telegram.handler.registration.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.ActivityTypeStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.util.UserStateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_TYPE;

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

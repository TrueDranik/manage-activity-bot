package tg.bot.activity.api.telegram.handler.registration.activity.format;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.api.telegram.handler.registration.activity.format.states.ActivityFormatMessageProcessor;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;
import tg.bot.activity.common.enums.states.StateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.util.MessageProcessorUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartFillingActivityFormat implements ActivityFormatHandleRegistration {

    private final UserStateCache userStateCache;
    private final Map<ActivityFormatStateEnum, ActivityFormatMessageProcessor> activityFormatMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);
        ActivityFormat activityFormat = (ActivityFormat) userState.getEntity();

        if (userState.getState().equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT)) {
            userState.setState(ActivityFormatStateEnum.START_PROCESSING);
        }

        ActivityFormatStateEnum activityCurrentState = (ActivityFormatStateEnum) userStateCache.getByChatId(chatId).getState();
        MessageProcessor messageProcessor = activityFormatMessageProcessorMap.get(activityCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, activityFormat);
    }

    @Override
    public StateEnum<?> getType() {
        return ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
    }
}

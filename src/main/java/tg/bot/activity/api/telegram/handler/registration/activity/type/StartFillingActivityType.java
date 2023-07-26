package tg.bot.activity.api.telegram.handler.registration.activity.type;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.api.telegram.handler.registration.activity.type.states.ActivityTypeMessageProcessor;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ActivityTypeStateEnum;
import tg.bot.activity.common.enums.states.StateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.util.MessageProcessorUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartFillingActivityType implements ActivityTypeHandleRegistration {

    private final UserStateCache userStateCache;
    private final Map<ActivityTypeStateEnum, ActivityTypeMessageProcessor> activityTypeMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByChatId(chatId);
        ActivityType activityType = (ActivityType) userState.getEntity();

        if (userState.getState().equals(ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE)) {
            userState.setState(ActivityTypeStateEnum.START_PROCESSING);
        }

        ActivityTypeStateEnum activityTypeCurrentState = (ActivityTypeStateEnum) userStateCache.getByChatId(chatId).getState();
        MessageProcessor messageProcessor = activityTypeMessageProcessorMap.get(activityTypeCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, activityType);
    }

    @Override
    public StateEnum<?> getType() {
        return ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
    }
}

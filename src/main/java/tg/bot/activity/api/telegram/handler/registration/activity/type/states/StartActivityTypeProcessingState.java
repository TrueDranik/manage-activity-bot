package tg.bot.activity.api.telegram.handler.registration.activity.type.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ActivityTypeStateEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.service.MessageService;

@Component
@RequiredArgsConstructor
public class StartActivityTypeProcessingState implements ActivityTypeMessageProcessor {

    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);
        userState.setState(ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityFormatOrTypeName());
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
    public ActivityTypeStateEnum getCurrentState() {
        return ActivityTypeStateEnum.START_PROCESSING;
    }
}

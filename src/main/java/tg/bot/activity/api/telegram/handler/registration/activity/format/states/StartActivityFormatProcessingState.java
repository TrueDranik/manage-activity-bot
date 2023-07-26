package tg.bot.activity.api.telegram.handler.registration.activity.format.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.service.MessageService;

@Component
@RequiredArgsConstructor
public class StartActivityFormatProcessingState implements ActivityFormatMessageProcessor {

    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);
        userState.setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityFormatOrTypeName() + activityMessageProperties.getActivityNameWarning());
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
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.START_PROCESSING;
    }
}

package tg.bot.activity.api.telegram.handler.registration.activity.format.states;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityFormatStateEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class AskFormatDescriptionState implements ActivityFormatMessageProcessor {
    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;

    String activityFormatDescription = "";

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) throws IOException, ExecutionException, InterruptedException {
        var chatId = message.getChatId();

        UserState stateCache = userStateCache.getByChatId(chatId);
        stateCache.setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_PHOTO);

        ActivityFormat activityFormat = (ActivityFormat) entity;
        activityFormat.setDescription(activityFormatDescription);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getSendPhoto());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        int lengthDescription = activityFormatDescription.length();
        return messageService.buildReplyMessage(chatId,
                activityMessageProperties.getFormatDescriptionTooLong().formatted(lengthDescription));
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        activityFormatDescription = message.getText();
        return activityFormatDescription.length() > 255;
    }

    @Override
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_DESCRIPTION;
    }
}

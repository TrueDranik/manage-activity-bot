package tg.bot.activity.api.telegram.handler.registration.activity.format.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.Album;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;
import tg.bot.activity.service.files.AlbumService;

@Component
@RequiredArgsConstructor
public class AskFormatNameState implements ActivityFormatMessageProcessor {

    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;
    private final UserStateCache userStateCache;
    private final AlbumService albumService;

    String activityNameFromUserAnswer;
    boolean isNameTooLong;
    Long chatId;
    UserState userCacheByTelegramId;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityFormat) {
        chatId = message.getChatId();
        activityNameFromUserAnswer = message.getText();
        userStateCache.getByChatId(chatId).setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_DESCRIPTION);
        boolean isActivityFormatForUpdate = userCacheByTelegramId.isForUpdate();

        if (isActivityFormatForUpdate) {
            updateActivityFormatName(activityFormat);
            activityFormatService.save((ActivityFormat) activityFormat);
        } else {
            createActivityFormat(activityFormat, message);
        }

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getSendFormatDescription());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        if (isNameTooLong) {
            return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameTooLong());
        } else {
            return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
        }
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();
        userCacheByTelegramId = userStateCache.getByChatId(message.getChatId());
        if (message.getText().length() > 15) {
            isNameTooLong = true;
            return true;
        }
        boolean existsActivityFormatByNameEqualsIgnoreCase = activityFormatService.existsByNameEqualsIgnoreCase(userAnswer);

        if (userCacheByTelegramId.isForUpdate() && existsActivityFormatByNameEqualsIgnoreCase) {
            ActivityFormat activityFormat = (ActivityFormat) userCacheByTelegramId.getEntity();
            return !activityFormatService.findActivityFormatByNameIgnoreCase(userAnswer).getId().equals(activityFormat.getId());
        }

        if (existsActivityFormatByNameEqualsIgnoreCase) {
            ActivityFormat activityFormatByNameIgnoreCase = activityFormatService.findActivityFormatByNameIgnoreCase(userAnswer);
            return activityFormatByNameIgnoreCase.getIsActive();
        } else {
            return false;
        }
    }

    @Override
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME;
    }

    private void updateActivityFormatName(Object activityFormat) {
        String name = StringUtils.capitalize(activityNameFromUserAnswer);
        ((ActivityFormat) activityFormat).setName(name);
    }

    private void createActivityFormat(Object activityFormat, Message message) {
        boolean existsActivityFormatByNameEqualsIgnoreCase = activityFormatService.existsByNameEqualsIgnoreCase(activityNameFromUserAnswer);
        if (existsActivityFormatByNameEqualsIgnoreCase) {
            ActivityFormat activityFormatByNameIgnoreCase = activityFormatService.findActivityFormatByNameIgnoreCase(activityNameFromUserAnswer);

            boolean hasActivityFormatByNameIgnoreCaseIsActive = activityFormatByNameIgnoreCase.getIsActive();
            if (!hasActivityFormatByNameIgnoreCaseIsActive) {
                activateActivity(activityFormatByNameIgnoreCase);
            }
        } else {
            createNewActivityFormat(activityFormat, message);
        }
    }

    private void activateActivity(ActivityFormat formatToActivate) {
        formatToActivate.setIsActive(true);
        Album albumForCurrentActivity = formatToActivate.getAlbum();
        albumForCurrentActivity.setIsActive(true);
        activityFormatService.save(formatToActivate);
        albumService.save(albumForCurrentActivity);
        userStateCache.getByChatId(chatId).setEntity(formatToActivate);
    }

    private void createNewActivityFormat(Object activityFormat, Message message) {
        String name = StringUtils.capitalize(message.getText());
        ((ActivityFormat) activityFormat).setName(name);
        ((ActivityFormat) activityFormat).setIsActive(true);
    }
}

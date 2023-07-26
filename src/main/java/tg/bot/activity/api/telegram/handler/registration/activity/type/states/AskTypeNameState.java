package tg.bot.activity.api.telegram.handler.registration.activity.type.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ActivityTypeStateEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.activity.type.impl.ActivityTypeServiceImpl;
import tg.bot.activity.util.KeyboardUtil;

@Component
@RequiredArgsConstructor
public class AskTypeNameState implements ActivityTypeMessageProcessor {

    private final MessageService messageService;
    private final ActivityTypeServiceImpl activityTypeService;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityType) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        boolean existsActivityFormatByNameEqualsIgnoreCase = activityTypeService.existsByNameEqualsIgnoreCase(userAnswer);
        if (existsActivityFormatByNameEqualsIgnoreCase) {
            ActivityType activityFormatByNameIgnoreCase = activityTypeService.findActivityTypeByNameIgnoreCase(userAnswer);

            boolean hasActivityFormatByNameIgnoreCaseIsActive = activityFormatByNameIgnoreCase.getIsActive();
            if (!hasActivityFormatByNameIgnoreCaseIsActive) {
                activityFormatByNameIgnoreCase.setIsActive(true);
                activityTypeService.save(activityFormatByNameIgnoreCase);
            }
        } else {
            ((ActivityType) activityType).setName(message.getText());
            ((ActivityType) activityType).setIsActive(true);

            activityTypeService.save((ActivityType) activityType);
        }

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .inlineKeyboardMarkupWithOneButton(CallbackEnum.SUP_ACTIVITY_TYPE.toString(), mainMessageProperties.getDone());

        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(activityMessageProperties.getRegisteredActivity(), userAnswer),
                keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();

        boolean existsActivityFormatByNameEqualsIgnoreCase = activityTypeService.existsByNameEqualsIgnoreCase(userAnswer);
        if (existsActivityFormatByNameEqualsIgnoreCase) {
            ActivityType activityFormatByNameIgnoreCase = activityTypeService.findActivityTypeByNameIgnoreCase(userAnswer);

            return activityFormatByNameIgnoreCase.getIsActive();
        } else {
            return false;
        }
    }

    @Override
    public ActivityTypeStateEnum getCurrentState() {
        return ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME;
    }
}

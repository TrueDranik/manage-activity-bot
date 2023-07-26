package tg.bot.activity.api.telegram.handler.registration.activity.type.states;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.ActivityTypeStateEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.activity.type.impl.ActivityTypeServiceImpl;
import com.bot.sup.util.KeyboardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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

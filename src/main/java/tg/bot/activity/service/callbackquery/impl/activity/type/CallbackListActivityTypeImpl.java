package tg.bot.activity.service.callbackquery.impl.activity.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.service.activity.type.impl.ActivityTypeServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackListActivityTypeImpl implements Callback {

    private final ActivityTypeServiceImpl activityTypeService;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttonActivityType = new ArrayList<>();
        List<ActivityType> activityTypes = activityTypeService.findAll();

        if (activityTypes.isEmpty()) {
            buttonActivityType.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(mainMessageProperties.getBack())
                            .callbackData(CallbackEnum.SUP_ACTIVITY_TYPE.toString())
                            .build()));

            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                    .keyboard(buttonActivityType)
                    .build();

            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(activityMessageProperties.getEmptyActivity())
                    .replyMarkup(keyboardMarkup)
                    .build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithActivity(activityTypes))
                .text(activityMessageProperties.getListActivityFormat())
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityType> activityTypes) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> mainRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        activityTypes.forEach(i -> {
            if (Boolean.TRUE.equals(i.getIsActive())) {
                mainRow.add(InlineKeyboardButton.builder()
                        .text(i.getName())
                        .callbackData(CallbackEnum.ACTIVITY_TYPE_OPTION + "/" + i.getId())
                        .build());
                if (mainRow.size() == 2) {
                    List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(mainRow);
                    mainKeyboard.add(temporaryKeyboardRow);
                    mainRow.clear();
                }
            }
        });

        if (mainRow.size() == 1) {
            mainKeyboard.add(mainRow);
        }

        secondRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SUP_ACTIVITY_TYPE.toString())
                .build());
        mainKeyboard.add(secondRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.LIST_ACTIVITY_TYPE;
    }
}

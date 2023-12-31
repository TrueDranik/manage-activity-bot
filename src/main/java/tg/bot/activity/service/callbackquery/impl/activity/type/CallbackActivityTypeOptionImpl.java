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
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.service.activity.type.impl.ActivityTypeServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.activity.common.enums.CallbackEnum.ACTIVITY_TYPE_OPTION;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeOptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityTypeServiceImpl activityTypeService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityTypeId = callbackQuery.getData().split("/")[1];

        ActivityType activityType = activityTypeService.findActivityTypeById(Long.valueOf(activityTypeId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityType.getName())
                .replyMarkup(setUpKeyboard(activityTypeId))
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard(String activityTypeId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.ACTIVITY_TYPE_CHANGE + "/" + activityTypeId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_ACTIVITY_TYPE + "/" + activityTypeId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_TYPE.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITY_TYPE_OPTION;
    }
}

package tg.bot.activity.service.callbackquery.impl.activity.format;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.activity.common.enums.CallbackEnum.ACTIVITY_FORMAT_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackActivityFormatOptionImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        ActivityFormat activityFormat = activityFormatService.findActivityFormatById(Long.valueOf(activityFormatId));
        String validActivityFormatName;
        if (activityFormat == null) {
            validActivityFormatName = "Формат активности не найден";
        } else {
            String activityFormatName = "*" + activityFormat.getName() + "*";
            String activityFormatDescription = "Описание: " + activityFormat.getDescription();

            validActivityFormatName = activityFormatName + "\n" + activityFormatDescription;
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(validActivityFormatName)
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(generateKeyboardWithActivity(activityFormatId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(String activityFormatId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.ACTIVITY_FORMAT_CHANGE + "/" + activityFormatId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_ACTIVITY_FORMAT + "/" + activityFormatId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_FORMAT.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITY_FORMAT_OPTION;
    }
}

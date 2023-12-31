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
import tg.bot.activity.service.callbackquery.Callback;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.activity.common.enums.CallbackEnum.SUP_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityMessageProperties.getActivityType())
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
// todo IKB builder вынеси в метод
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getListActivityType())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_TYPE.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getAddActivityType())
                        .callbackData(CallbackEnum.CHOOSE_ACTIVITY_FORMAT_TO_TYPE.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.SUP_ACTIVITIES.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return SUP_ACTIVITY_TYPE;
    }
}

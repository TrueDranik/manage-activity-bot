package tg.bot.activity.service.callbackquery.impl.activity.type;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_TYPE;
import static com.bot.sup.common.enums.CallbackEnum.CHOOSE_ACTIVITY_FORMAT_TO_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityTypeImpl implements Callback {
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .text(activityMessageProperties.getChooseFormatToBind())
                .replyMarkup(createInlineKeyboard())
                .build();
    }
    public InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<ActivityFormat> activityFormats = activityFormatRepository.findActivityFormatsByIsActiveIsTrue();
        for(ActivityFormat activityFormat : activityFormats) {
            buttons.add(List.of(InlineKeyboardButton.builder()
                    .text(activityFormat.getName())
                    .callbackData(ADD_ACTIVITY_TYPE + "/"+ activityFormat.getId())
                    .build()));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CHOOSE_ACTIVITY_FORMAT_TO_TYPE;
    }
}

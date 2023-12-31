package tg.bot.activity.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KeyboardUtil {

    public static InlineKeyboardMarkup inlineKeyboardMarkupWithOneButton(String callbackData, String text) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData(callbackData)
                        .text(text)
                        .build()
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}

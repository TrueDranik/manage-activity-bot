package tg.bot.activity.api.telegram.handler.registration.information.states;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.InformationAboutUsStateEnum;
import com.bot.sup.model.entity.AboutUs;
import com.bot.sup.service.aboutUs.AboutUsService;
import com.bot.sup.service.MessageService;
import com.bot.sup.util.KeyboardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class AskFullInformationDescriptionState implements InformationAboutUsMessageProcessor {
    private final MessageService messageService;
    private final AboutUsService aboutUsService;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        ((AboutUs) entity).setFullDescription(message.getText());

        aboutUsService.save((AboutUs) entity);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil.inlineKeyboardMarkupWithOneButton(CallbackEnum.MENU.toString(), "Меню");

        return messageService.getReplyMessageWithKeyboard(chatId, "✅ Успешно обновили информацию!", keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, "Кол-во символов превышает 1024!\nВведите снова.");
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String userAnswer = message.getText();

        return userAnswer.length() > 1024;
    }

    @Override
    public InformationAboutUsStateEnum getCurrentState() {
        return InformationAboutUsStateEnum.ASK_FULL_DESCRIPTION;
    }
}

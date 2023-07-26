package tg.bot.activity.service.callbackquery.impl.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.instructor.InstructorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackListInstructorsImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorService instructorService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> buttonEmptyInstructors = new ArrayList<>();
        List<Instructor> instructor = instructorService.findAll();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (callbackQuery.getMessage().hasPhoto()) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(instructorMessageProperties.getListInstructor())
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(generateKeyboardWithInstructors(instructor))
                    .build();

        } else if (instructor.isEmpty()) {
            buttonEmptyInstructors.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(mainMessageProperties.getBack())
                            .callbackData(CallbackEnum.INSTRUCTORS.toString())
                            .build()
            ));
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                    .keyboard(buttonEmptyInstructors)
                    .build();

            return SendMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(instructorMessageProperties.getEmptyInstructors())
                    .replyMarkup(keyboard)
                    .parseMode(ParseMode.MARKDOWN)
                    .build();
        }

        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .replyMarkup(generateKeyboardWithInstructors(instructor))
                .text(instructorMessageProperties.getListInstructor())
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(List<Instructor> instructor) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        instructor.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getFirstName() + " " + i.getLastName())
                            .callbackData(CallbackEnum.INSTRUCTOR_OPTION + "/" + i.getId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.INSTRUCTORS.toString())
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.LIST_INSTRUCTORS;
    }
}

package tg.bot.activity.service.callbackquery.impl.instructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.model.entity.Schedule;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.instructor.InstructorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackDeleteInstructorImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorService instructorService;

    @Transactional
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        Instructor instructorToDelete = instructorService.findInstructorsById(Long.parseLong(instructorId));
        String responseText = createResponseText(instructorToDelete);

        instructorService.deleteInstructor(instructorToDelete);

        return SendMessage.builder()
                .text(responseText)
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteInstructor())
                .build();

    }

    public String createResponseText(Instructor instructorToDelete) {
        if (instructorToDelete.getSchedules() == null) {
            return instructorMessageProperties.getDeleteInstructor();
        }
        Set<Schedule> scheduleFromInstructorToDelete = instructorToDelete.getSchedules();
        StringBuilder result = new StringBuilder();
        for (Schedule schedule : scheduleFromInstructorToDelete) {
            result.append(schedule.toString());
        }
        return instructorMessageProperties.getDeleteInstructor() + "\nИнструктор был привязан к расписаниям: \n" + result;
    }

    private InlineKeyboardMarkup createKeyboardForDeleteInstructor() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_INSTRUCTORS.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.DELETE_INSTRUCTOR;
    }
}

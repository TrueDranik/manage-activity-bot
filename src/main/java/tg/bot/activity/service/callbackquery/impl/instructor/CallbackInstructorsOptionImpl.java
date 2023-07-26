package tg.bot.activity.service.callbackquery.impl.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.PhotoSizeEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.files.photo.PhotoService;
import tg.bot.activity.service.instructor.InstructorService;
import tg.bot.activity.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import static tg.bot.activity.common.enums.CallbackEnum.INSTRUCTOR_OPTION;

@Service
@RequiredArgsConstructor
public class CallbackInstructorsOptionImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final InstructorService instructorService;
    private final PhotoService photoService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        Instructor instructor = instructorService.findInstructorsById(Long.valueOf(instructorId));
        var instructorPhotoUrl = photoService.getImageUrl(instructor.getPhoto().getId(), PhotoSizeEnum.LARGE);

        InputFile photoToSent = ImageUtil.getPhotoToSend(instructorPhotoUrl, instructor.getPhoto().getNameFromRequest());

        return SendPhoto.builder()
                .chatId(chatId)
                .caption(instructorInfo(instructor))
                .photo(photoToSent)
                .parseMode(ParseMode.HTML)
                .replyMarkup(generateKeyboardWithInstructors(instructorId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(String instructorId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.CHANGE_INSTRUCTOR + "/" + instructorId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_INSTRUCTOR + "/" + instructorId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_INSTRUCTORS.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    private String instructorInfo(Instructor instructor) {
        String userId = String.format("<a href=\"tg://user?id=%s\"> (профиль)</a>", instructor.getTelegramId().toString());
        return "\uD83E\uDEAA ФИ: " + instructor.getFirstName() + " " + instructor.getLastName() + userId
                + "\n☎️ Номер телефона: " + instructor.getPhoneNumber()
                + "\n\uD83C\uDF10 Имя пользователя: @" + (instructor.getUsername() != null ? instructor.getUsername() : "");
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return INSTRUCTOR_OPTION;
    }
}

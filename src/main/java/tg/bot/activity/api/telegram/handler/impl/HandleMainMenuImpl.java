package tg.bot.activity.api.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import tg.bot.activity.api.telegram.handler.Handle;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.TelegramProperties;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.common.properties.message.ScheduleMessageProperties;
import tg.bot.activity.model.entity.SelectedSchedule;
import tg.bot.activity.repository.SelectedScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandleMainMenuImpl implements Handle {

    private final SelectedScheduleRepository selectedScheduleRepository;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    private final TelegramProperties telegramProperties;

    @Transactional
    @Override
    public BotApiMethod<?> getMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        Optional<SelectedSchedule> selectedActivity = selectedScheduleRepository.findByTelegramId(chatId);
        if (selectedActivity.isPresent()) {
            selectedScheduleRepository.deleteByTelegramId(chatId);
        }

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(mainMessageProperties.getUserChoose())
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    public InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getSchedules())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(instructorMessageProperties.getInstructors())
                .callbackData(CallbackEnum.INSTRUCTORS.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(activityMessageProperties.getActivities())
                .callbackData(CallbackEnum.SUP_ACTIVITIES.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getTourEditor())
                .callbackData(CallbackEnum.SCHEDULE_WEBAPP.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getInfoAboutPayment())
                .webApp(new WebAppInfo(telegramProperties.getWebAppInfoAboutPayment()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getGallery())
                .webApp(new WebAppInfo(telegramProperties.getWebAppGallery()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getAboutUs())
                .webApp(new WebAppInfo(telegramProperties.getWebAppAboutUs()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getReviews())
                .webApp(new WebAppInfo(telegramProperties.getWebAppReviews()))
                .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}

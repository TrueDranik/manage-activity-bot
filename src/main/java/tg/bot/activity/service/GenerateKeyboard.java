package tg.bot.activity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.api.telegram.handler.impl.HandleMainMenuImpl;
import tg.bot.activity.api.telegram.handler.impl.HandleScheduleInfoImpl;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.common.properties.message.ScheduleMessageProperties;
import tg.bot.activity.model.entity.Activity;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.ActivityType;
import tg.bot.activity.model.entity.Route;
import tg.bot.activity.model.entity.Schedule;
import tg.bot.activity.repository.ScheduleRepository;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerateKeyboard {

    private final Bot bot;
    private final MainMessageProperties mainMessageProperties;
    private final HandleMainMenuImpl handleMainMenu;
    private final HandleScheduleInfoImpl handleScheduleInfo;
    private final ScheduleRepository scheduleRepository;
    private final ActivityFormatServiceImpl activityFormatService;
    private final ScheduleMessageProperties scheduleMessageProperties;

    public Integer getMessageId(Long chatId) {
        Update update = bot.getUserUpdate();
        if (update.getCallbackQuery().getMessage().getChatId().equals(chatId)) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }

        return null;
    }

    public void mainMenu(Long chatId, Integer messageId) throws TelegramApiException {
        bot.execute(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(mainMessageProperties.getUserChoose())
                        .replyMarkup(handleMainMenu.createInlineKeyboard())
                        .build());
    }

    public void scheduleMenu(Long chatId, Integer messageId) throws TelegramApiException {
        List<ActivityFormat> activityFormats = activityFormatService.findAll();
        bot.execute(
                EditMessageText.builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .text(scheduleMessageProperties.getMenuSchedules())
                        .replyMarkup(generateKeyboardWithActivity(activityFormats))
                        .build());
    }

    public void scheduleInfo(Long chatId, Integer messageId, String activityFormatId, String eventDate, String scheduleId) throws TelegramApiException {
        Schedule schedule = scheduleRepository.findById(Long.parseLong(scheduleId))
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id [" + scheduleId + "] not found"));

        Optional<Activity> optionalActivity = Optional.ofNullable(schedule.getActivity());
        Optional<Route> optionalRoute = Optional.ofNullable(schedule.getRoute());

        bot.execute(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Дата и время старта: " + schedule.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " " + LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " ("
                                + schedule.getEventDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))
                                + ")\n"
                                + "Формат активности: " + optionalActivity.map(Activity::getActivityFormat)
                                .filter(format -> format.getName() != null).map(ActivityFormat::getName).orElse("Не найдено!") + "\n"
                                + "Тип активности: " + optionalActivity.map(Activity::getActivityType)
                                .filter(type -> type.getName() != null).map(ActivityType::getName).orElse("Не найдено!") + "\n"
                                + "Название маршрута: " + optionalRoute.map(Route::getName).orElse("Не найдено!") + "\n"
                                + "Точка старта: " + optionalRoute.map(Route::getStartPointName).orElse("Не найдено!") + "\n"
                                + "Координаты старта: " + optionalRoute.map(Route::getStartPointCoordinates).orElse("Не найдено!"))
                        .parseMode(ParseMode.MARKDOWN)
                        .replyMarkup(handleScheduleInfo.createInlineKeyboard(activityFormatId, eventDate, scheduleId))
                        .build());
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityFormat> activityFormats) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activityFormats.forEach(i -> {
                    if (Boolean.TRUE.equals(i.getIsActive())) {
                        rowMain.add(InlineKeyboardButton.builder()
                                .text(i.getName())
                                .callbackData(CallbackEnum.ACTIVITYFORMAT_TO_DATE + "/" + i.getId())
                                .build());
                        if (rowMain.size() == 2) {
                            List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                            mainKeyboard.add(temporaryKeyboardRow);
                            rowMain.clear();
                        }
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.MENU.toString())
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }
}

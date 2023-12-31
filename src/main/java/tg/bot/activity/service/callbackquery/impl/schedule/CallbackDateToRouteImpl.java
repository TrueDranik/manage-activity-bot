package tg.bot.activity.service.callbackquery.impl.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.common.properties.message.ScheduleMessageProperties;
import tg.bot.activity.model.entity.Schedule;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.schedule.impl.ScheduleServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CallbackDateToRouteImpl implements Callback {

    private final ScheduleServiceImpl scheduleService;
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;

    @Transactional
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] callbackQueryData = callbackQuery.getData().split("/");
        String activityFormatId = callbackQueryData[1];
        String eventDate;
        String scheduleId;

        if (callbackQueryData.length <= 3) {
            eventDate = callbackQueryData[2];
        } else {
            scheduleId = callbackQueryData[3];
            Schedule scheduleById = scheduleService.findScheduleById(Long.valueOf(scheduleId));
            eventDate = scheduleById.getEventDate().toString();
        }

        String datePattern = "dd.MM.yyyy";

        scheduleService.deleteSelectedScheduleByTelegramId(chatId);

        List<Schedule> schedules = scheduleService.findScheduleByActivityFormatIdAndEventDate(Long.valueOf(activityFormatId), LocalDate.parse(eventDate));

        if (createInlineKeyboard(schedules, activityFormatId, eventDate).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getNotFoundDate(),
                            LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern))))
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                    .build();
        }

        if (callbackQuery.getMessage().hasPhoto()) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getChooseRouteForDate(),
                            LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern)),
                            LocalDate.parse(eventDate).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))))
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                    .build();

        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(String.format(scheduleMessageProperties.getChooseRouteForDate(),
                        LocalDate.parse(eventDate).format(DateTimeFormatter.ofPattern(datePattern)),
                        LocalDate.parse(eventDate).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru"))))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(createInlineKeyboard(schedules, activityFormatId, eventDate))
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<Schedule> schedules, String activityFormatId, String eventDate) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        schedules.forEach(i -> {
            if (i.getIsActive().equals(true)) {
                rowMain.add(InlineKeyboardButton.builder()
                        .text(i.getRoute().getName() + "|" + i.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .callbackData(CallbackEnum.SCHEDULE_INFO + "/" + activityFormatId + "/" + eventDate + "/" + i.getId())
                        .build());
                if (rowMain.size() == 2) {
                    List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                    mainKeyboard.add(temporaryKeyboardRow);
                    rowMain.clear();
                }
            }
        });

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.ACTIVITYFORMAT_TO_DATE + "/" + activityFormatId + "/" + eventDate)
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.DATE_TO_ROUTE;
    }
}

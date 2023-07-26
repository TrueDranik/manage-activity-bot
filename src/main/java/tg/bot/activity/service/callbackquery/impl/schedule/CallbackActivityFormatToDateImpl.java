package tg.bot.activity.service.callbackquery.impl.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.common.properties.message.ScheduleMessageProperties;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.Schedule;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.schedule.impl.ScheduleServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackActivityFormatToDateImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ScheduleServiceImpl scheduleService;
    private final ActivityFormatServiceImpl activityFormatService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        ActivityFormat activityFormat = activityFormatService.findActivityFormatById(Long.valueOf(activityFormatId));
        List<Schedule> eventDate = scheduleService.getSchedulesByActivityFormatId(Long.valueOf(activityFormatId));

        // todo вынеси в метод
        if (generateKeyboardWithSchedule(eventDate, activityFormatId).getKeyboard().size() <= 1) {
            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(chatId)
                    .text(String.format(scheduleMessageProperties.getNotFoundFormat(), activityFormat.getName()))
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(generateKeyboardWithSchedule(eventDate, activityFormatId))
                    .build();
        }

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(String.format(scheduleMessageProperties.getDateChoice(), activityFormat.getName()))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(generateKeyboardWithSchedule(eventDate, activityFormatId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithSchedule(List<Schedule> eventDate, String activityFormatId) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Set<LocalDate> localDates = new HashSet<>();
        for (Schedule schedule : eventDate) {
            if (schedule.getEventDate().isBefore(LocalDate.now())
                    || schedule.getIsActive().equals(false)) {
                continue;
            }

            localDates.add(schedule.getEventDate());
        }

        localDates.forEach(i -> {
            rowMain.add(InlineKeyboardButton.builder()
                    .text(i.format(formatter) + " (" +
                            i.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("Ru")) + ")")
                    .callbackData(CallbackEnum.DATE_TO_ROUTE + "/" + activityFormatId + "/" + i)
                    .build());
            if (rowMain.size() == 2) {
                List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                mainKeyboard.add(temporaryKeyboardRow);
                rowMain.clear();
            }
        });

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT + "/" + activityFormatId)
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITYFORMAT_TO_DATE;
    }
}

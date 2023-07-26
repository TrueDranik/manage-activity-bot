package tg.bot.activity.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.impl.HandleScheduleInfoImpl;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.PhotoSizeEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Photo;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.service.callbackquery.AbstracktCallbackHandler;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.files.photo.PhotoService;
import com.bot.sup.service.schedule.impl.ScheduleServiceImpl;
import com.bot.sup.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackScheduleInfoImpl extends AbstracktCallbackHandler implements Callback {
    private final PhotoService photoService;
    private final ScheduleServiceImpl scheduleService;
    private final HandleScheduleInfoImpl handleScheduleInfo;
    private String activityFormatIdFromCallback;
    private String scheduleIdFromCallback;
    private String eventDateFromCallback;
    private Optional<Activity> optionalActivity;
    private Schedule selectedSchedule;
    private Optional<Route> optionalRoute;
    private InputFile photoToSent;
    private SendPhoto responseToUser;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {

        handleCallback(callbackQuery);

        return responseToUser;
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_INFO;
    }

    @Override
    public void mapInputParameters(CallbackQuery callbackQuery) {

        String[] callbackQueryDatas = callbackQuery.getData().split("/");
        activityFormatIdFromCallback = callbackQueryDatas[1];
        eventDateFromCallback = callbackQueryDatas[2];
        scheduleIdFromCallback = callbackQueryDatas[3];
    }

    @Override
    public void saveState(CallbackQuery callbackQuery) {
        SelectedSchedule requestedSchedule = new SelectedSchedule();
        requestedSchedule.setTelegramId(callbackQuery.getMessage().getChatId());
        requestedSchedule.setCurrentScheduleId(Long.valueOf(scheduleIdFromCallback));
        scheduleService.saveSelectedSchedule(requestedSchedule);

    }

    @Override
    public void processBusinessLogic(CallbackQuery callbackQuery) {

        selectedSchedule = scheduleService.findScheduleById(Long.valueOf(scheduleIdFromCallback));

        optionalActivity = Optional.ofNullable(selectedSchedule.getActivity());
        optionalRoute = Optional.ofNullable(selectedSchedule.getRoute());
        Photo routePhoto = photoService.findPhotoByRouteId(optionalRoute.orElseThrow(EntityNotFoundException::new).getId())
                .orElseThrow(EntityNotFoundException::new);
        String urlPhotoToSend = photoService.getImageUrl(routePhoto.getId(), PhotoSizeEnum.LARGE);

        photoToSent = ImageUtil.getPhotoToSend(urlPhotoToSend, routePhoto.getNameFromRequest());
    }

    @Override
    public void mappingOutputParameter(CallbackQuery callbackQuery) {
        responseToUser = SendPhoto.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .photo(photoToSent)
                .caption(HandleScheduleInfoImpl.scheduleInfo(eventDateFromCallback, selectedSchedule, optionalActivity, optionalRoute))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(handleScheduleInfo.createInlineKeyboard(activityFormatIdFromCallback, eventDateFromCallback, scheduleIdFromCallback))
                .build();
    }
}

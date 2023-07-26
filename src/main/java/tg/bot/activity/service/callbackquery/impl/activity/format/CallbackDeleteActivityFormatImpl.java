package tg.bot.activity.service.callbackquery.impl.activity.format;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.service.activity.format.ActivityFormatService;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.service.files.AlbumService;
import tg.bot.activity.service.files.photo.PhotoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static tg.bot.activity.common.enums.CallbackEnum.DELETE_ACTIVITY_FORMAT;

@RequiredArgsConstructor
@Service
public class CallbackDeleteActivityFormatImpl implements Callback {

    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatService activityFormatService;
    private final PhotoService photoService;
    private final AlbumService albumService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Long activityFormatId = Long.valueOf(callbackQuery.getData().split("/")[1]);
        Long albumIdToDelete = activityFormatService.getActivityFormatById(activityFormatId).getAlbumId();
        Long iconIdToDelete = activityFormatService.getIconId(activityFormatId);

        activityFormatService.deleteActivityFormat(activityFormatId);
        albumService.deleteAlbum(albumIdToDelete);

        if (iconIdToDelete != null) {
            photoService.deletePhotoById(iconIdToDelete);
        }

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text(activityMessageProperties.getDeleteActivity())
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteActivity())
                .build();
    }

    private InlineKeyboardMarkup createKeyboardForDeleteActivity() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_FORMAT.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return DELETE_ACTIVITY_FORMAT;
    }
}

package tg.bot.activity.api.telegram.handler.registration.activity.format.states;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.ActivityFormatStateEnum;
import tg.bot.activity.common.properties.TelegramProperties;
import tg.bot.activity.common.properties.message.ActivityMessageProperties;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.ActivityFormat;
import tg.bot.activity.model.entity.Album;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.repository.AlbumRepository;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.activity.format.impl.ActivityFormatServiceImpl;
import tg.bot.activity.service.files.AlbumService;
import tg.bot.activity.service.files.MultipartFileConstructor;
import tg.bot.activity.service.files.photo.PhotoService;
import tg.bot.activity.util.ImageUtil;
import tg.bot.activity.util.KeyboardUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class AskFormatPhotoState implements ActivityFormatMessageProcessor {

    public static final String IMAGE_SVG_XML = "image/svg+xml";
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatServiceImpl activityFormatService;
    private final MainMessageProperties mainMessageProperties;
    private final AlbumRepository albumRepository;
    private final TelegramProperties telegramProperties;
    private final UserStateCache userStateCache;
    private final PhotoRepository photoRepository;
    private final AlbumService albumService;
    private final PhotoService photoService;

    @Value("${ids.iconAlbumId}")
    private Long iconAlbumId;

    String photoName;
    String activityFormatName;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object activityFormat) throws ExecutionException, InterruptedException, IOException {
        Long chatId = message.getChatId();
        Document sentDoc = message.getDocument();
        activityFormatName = ((ActivityFormat) activityFormat).getName();
        photoName = activityFormatName + "-icon.svg";
        String botToken = telegramProperties.getBotToken();
        UserState userCacheByTelegramId = userStateCache.getByChatId(message.getChatId());
        boolean isActivityFormatForUpdate = userCacheByTelegramId.isForUpdate();

        byte[] iconData = null;
        if (sentDoc != null) {
            String fileId = sentDoc.getFileId();
            iconData = ImageUtil.uploadFile(fileId, botToken);
        }
        MultipartFile fileToUpload = MultipartFileConstructor.getMultipartFileFromByteArray(photoName, iconData);
        Photo formatIcon = photoService.savePhoto(fileToUpload, iconAlbumId);

        if (isActivityFormatForUpdate) {
            updateIcon(activityFormat, formatIcon);
            updateAlbumName(activityFormat);
            activityFormatService.save((ActivityFormat) activityFormat);
        } else {
            createNewIcon(activityFormat, formatIcon);
            createAlbum(activityFormat);
            activityFormatService.save((ActivityFormat) activityFormat);
        }

        String responseToUser = isActivityFormatForUpdate ? activityMessageProperties.getUpdatedActivity() : activityMessageProperties.getRegisteredActivity();

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .inlineKeyboardMarkupWithOneButton(CallbackEnum.SUP_ACTIVITY_FORMAT.toString(), mainMessageProperties.getDone());

        return messageService.getReplyMessageWithKeyboard(chatId,
                String.format(responseToUser, activityFormatName),
                keyboardMarkup);
    }

    private void updateIcon(Object activityFormat, Photo formatPhoto) {

        Long iconIdToDelete = ((ActivityFormat) activityFormat).getIcon().getId();
        ((ActivityFormat) activityFormat).setIcon(formatPhoto);
        photoRepository.save(formatPhoto);
        activityFormatService.save((ActivityFormat) activityFormat);
        photoRepository.deleteById(iconIdToDelete);
    }

    private void updateAlbumName(Object activityFormat) {

        Album currentActivityFormatAlbum = ((ActivityFormat) activityFormat).getAlbum();
        currentActivityFormatAlbum.setName(activityFormatName);
        albumRepository.save(currentActivityFormatAlbum);
    }

    private void createNewIcon(Object activityFormat, Photo formatPhoto) {

        ((ActivityFormat) activityFormat).setIcon(formatPhoto);
        photoRepository.save(formatPhoto);
    }

    private void createAlbum(Object activityFormat) {

        Album currentAlbum = ((ActivityFormat) activityFormat).getAlbum();

        if (currentAlbum != null) {
            currentAlbum.setIsActive(true);
            albumRepository.save(currentAlbum);
        } else {
            Long createdAlbumId = albumService.createAlbum(activityFormatName).getId();
            Album createdAlbum = albumRepository.findById(createdAlbumId)
                    .orElseThrow(() -> new RuntimeException("There is no album with id " + Arrays.toString(new Long[]{createdAlbumId})));
            ((ActivityFormat) activityFormat).setAlbum(createdAlbum);
        }
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, activityMessageProperties.getNotValidIconFormat());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        String iconType = null;
        if (message.getDocument() != null) {
            iconType = message.getDocument().getMimeType().toLowerCase();
        }
        return !IMAGE_SVG_XML.equals(iconType);
    }

    @Override
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_PHOTO;
    }
}

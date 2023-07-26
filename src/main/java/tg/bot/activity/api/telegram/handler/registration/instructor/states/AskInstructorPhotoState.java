package tg.bot.activity.api.telegram.handler.registration.instructor.states;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.common.properties.TelegramProperties;
import tg.bot.activity.common.properties.message.InstructorMessageProperties;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.model.entity.Photo;
import tg.bot.activity.repository.PhotoRepository;
import tg.bot.activity.service.MessageService;
import tg.bot.activity.service.files.MultipartFileConstructor;
import tg.bot.activity.service.files.photo.PhotoService;
import tg.bot.activity.service.instructor.InstructorService;
import tg.bot.activity.util.ImageUtil;
import tg.bot.activity.util.KeyboardUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class AskInstructorPhotoState implements InstructorMessageProcessor {

    private final AbsSender absSender;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;
    private final TelegramProperties telegramProperties;
    private final PhotoRepository photoRepository;
    private final InstructorService instructorService;
    private final PhotoService photoService;

    @Value("${ids.instructorAlbumId}")
    private Long instructorAlbumId;

    public AskInstructorPhotoState(@Lazy AbsSender absSender, MessageService messageService, InstructorMessageProperties instructorMessageProperties,
                                   UserStateCache userStateCache, TelegramProperties telegramProperties,
                                   PhotoRepository photoRepository, InstructorService instructorService, PhotoService photoService) {
        this.absSender = absSender;
        this.messageService = messageService;
        this.instructorMessageProperties = instructorMessageProperties;
        this.userStateCache = userStateCache;
        this.telegramProperties = telegramProperties;
        this.photoRepository = photoRepository;
        this.instructorService = instructorService;
        this.photoService = photoService;
    }

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) throws IOException, ExecutionException, InterruptedException {
        Long chatId = message.getChatId();
        List<PhotoSize> sentPhotos = message.getPhoto();
        String botToken = telegramProperties.getBotToken();
        UserState userCacheByTelegramId = userStateCache.getByChatId(chatId);

        String firstName = ((Instructor) instructor).getFirstName();
        String lastName = ((Instructor) instructor).getLastName();

        byte[] photoData = ImageUtil.uploadPhoto(sentPhotos, absSender, botToken);
        MultipartFile fileToUpload = MultipartFileConstructor.getMultipartFileFromByteArray(firstName + "-" + lastName  + ".png", photoData);

        Photo instructorPhoto = photoService.savePhoto(fileToUpload, instructorAlbumId);

        if (userCacheByTelegramId.isForUpdate()) {
            updatePhoto(instructor, instructorPhoto);
        } else {
            photoRepository.save(instructorPhoto);
            ((Instructor) instructor).setPhoto(instructorPhoto);
        }

        if (userCacheByTelegramId.isForUpdate()) {
            instructorService.save((Instructor) instructor);

            InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                    .inlineKeyboardMarkupWithOneButton(CallbackEnum.INSTRUCTORS.toString(), instructorMessageProperties.getMenuInstructors());

            return messageService.getReplyMessageWithKeyboard(chatId, "\uD83D\uDCA5 Инструктор обновлен!" +
                    instructorInfo(((Instructor) instructor)), keyboardMarkup);
        }
        userCacheByTelegramId.setState(InstructorStateEnum.ASK_TELEGRAM_ID);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getGetTelegramId());
    }

    private void updatePhoto(Object instructor, Photo instructorPhoto) {

        Long instructorPhotoIdToDelete = ((Instructor) instructor).getPhoto().getId();
        ((Instructor) instructor).setPhoto(instructorPhoto);
        photoRepository.save(instructorPhoto);
        instructorService.save((Instructor) instructor);
        photoRepository.deleteById(instructorPhotoIdToDelete);

    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return null;
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return false;
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_PHOTO;
    }

    private String instructorInfo(Instructor instructor) {
        return "ФИ: " + instructor.getFirstName() + " " + instructor.getLastName()
                + "\nНомер телефона: " + instructor.getPhoneNumber()
                + "\nИмя пользователя: @" + instructor.getUsername();
    }
}

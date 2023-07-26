package tg.bot.activity.api.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.service.GenerateKeyboard;

@CrossOrigin("*")
@RestController
@RequestMapping("/keyboard")
@RequiredArgsConstructor
public class GenerateKeyboardController {

    private final GenerateKeyboard generateKeyboard;

    @GetMapping("/message/{telegramId}")
    public Integer getMessageId(@PathVariable("telegramId") Long telegramId) {
        return generateKeyboard.getMessageId(telegramId);
    }

    @PostMapping("/main-menu")
    public void sendMaimMenu(Long telegramId, Integer messageId) throws TelegramApiException {
        generateKeyboard.mainMenu(telegramId, messageId);
    }

    @PostMapping("/schedule-menu")
    public void sendScheduleMenu(Long telegramId, Integer messageId) throws TelegramApiException {
        generateKeyboard.scheduleMenu(telegramId, messageId);
    }

    @PostMapping("/schedule-info")
    public void sendScheduleInfo(Long telegramId, Integer messageId, String activityFormatId, String eventDate, String scheduleId)
            throws TelegramApiException {
        generateKeyboard.scheduleInfo(telegramId, messageId, activityFormatId, eventDate, scheduleId);
    }
}

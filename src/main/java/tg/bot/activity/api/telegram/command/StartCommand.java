package tg.bot.activity.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import tg.bot.activity.api.telegram.handler.impl.HandleMainMenuImpl;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.properties.message.MainMessageProperties;

@Component
@RequiredArgsConstructor
public class StartCommand implements BaseCommand {

    private final HandleMainMenuImpl handleMainMenu;
    private final MainMessageProperties mainMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotCommand getBotCommand() {
        return BotCommand.builder()
                .command(mainMessageProperties.getCommandStart())
                .description(mainMessageProperties.getCommandStartDescription())
                .build();
    }

    @Override
    public BotApiMethod<?> getAction(Update update) {
        userStateCache.deleteFromCache(update.getMessage().getChatId());

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(mainMessageProperties.getUserChoose())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }
}

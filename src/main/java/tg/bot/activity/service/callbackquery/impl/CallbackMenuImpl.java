package tg.bot.activity.service.callbackquery.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.activity.api.telegram.handler.impl.HandleMainMenuImpl;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.properties.message.MainMessageProperties;
import tg.bot.activity.service.callbackquery.Callback;

@Service
@RequiredArgsConstructor
public class CallbackMenuImpl implements Callback {

    private final HandleMainMenuImpl handleMainMenu;
    private final MainMessageProperties mainMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        userStateCache.deleteFromCache(callbackQuery.getMessage().getChatId()); // TODO: 26.01.2023 мб вынести в отдельный метод или использовать @Cachable

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .text(mainMessageProperties.getUserChoose())
                .parseMode(ParseMode.MARKDOWN)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.MENU;
    }
}
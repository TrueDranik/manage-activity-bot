package tg.bot.activity.api.telegram.handler.registration.information.states;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InformationAboutUsStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.service.MessageService;

@Component
@RequiredArgsConstructor
public class StartInformationAboutUsProcessorState implements InformationAboutUsMessageProcessor {

    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByChatId(chatId);
        userState.setState(InformationAboutUsStateEnum.ASK_FULL_DESCRIPTION);

        return messageService.buildReplyMessage(chatId, "Введите информацию \"О нас\"");
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
    public InformationAboutUsStateEnum getCurrentState() {
        return InformationAboutUsStateEnum.START_PROCESSOR;
    }
}

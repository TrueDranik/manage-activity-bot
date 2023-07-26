package tg.bot.activity.api.telegram.handler.registration.information;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.activity.api.telegram.handler.registration.MessageProcessor;
import tg.bot.activity.api.telegram.handler.registration.information.states.InformationAboutUsMessageProcessor;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InformationAboutUsStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.AboutUs;
import tg.bot.activity.service.aboutUs.AboutUsService;
import tg.bot.activity.util.MessageProcessorUtil;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingInformationAboutUs implements InformationAboutUsHandleRegistration {

    private final AboutUsService aboutUsService;
    private final Map<InformationAboutUsStateEnum, InformationAboutUsMessageProcessor> aboutUsMessageProcessorMap;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        Optional<AboutUs> aboutUs = aboutUsService.findById(1L);
        UserState userState = userStateCache.getByChatId(chatId);

        if (userState.getState().equals(InformationAboutUsStateEnum.FILLING_ABOUT_US)) {
            userState.setState(InformationAboutUsStateEnum.START_PROCESSOR);
        }

        AboutUs ab = aboutUs.orElseGet(AboutUs::new);
        InformationAboutUsStateEnum aboutUsCurrentState = (InformationAboutUsStateEnum) userStateCache.getByChatId(chatId).getState();
        MessageProcessor messageProcessor = aboutUsMessageProcessorMap.get(aboutUsCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, ab);
    }

    @Override
    public StateEnum<?> getType() {
        return InformationAboutUsStateEnum.FILLING_ABOUT_US;
    }
}

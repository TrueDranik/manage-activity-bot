package tg.bot.activity.service.callbackquery.impl.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.activity.api.telegram.handler.registration.StateContext;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.CallbackEnum;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.service.callbackquery.Callback;
import tg.bot.activity.util.UserStateUtil;

import static tg.bot.activity.common.enums.CallbackEnum.ADD_INSTRUCTOR;

@Service
@RequiredArgsConstructor
public class CallbackAddInstructorImpl implements Callback {

    private final StateContext stateContext;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;
        Instructor instructor = new Instructor();

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, instructor, false);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ADD_INSTRUCTOR;
    }
}

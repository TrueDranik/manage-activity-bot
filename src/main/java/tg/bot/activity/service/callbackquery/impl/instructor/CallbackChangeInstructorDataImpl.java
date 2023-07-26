package tg.bot.activity.service.callbackquery.impl.instructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tg.bot.activity.service.instructor.InstructorService;
import tg.bot.activity.util.UserStateUtil;

import static tg.bot.activity.common.enums.CallbackEnum.CHANGE_INSTRUCTOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackChangeInstructorDataImpl implements Callback {

    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final InstructorService instructorService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;

        Instructor instructor = instructorService.findInstructorsById(Long.valueOf(instructorId));

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, instructor, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CHANGE_INSTRUCTOR;
    }
}

package tg.bot.activity.service.callbackquery;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public abstract class AbstracktCallbackHandler {

    public abstract void mapInputParameters(CallbackQuery callbackQuery);

    public abstract void saveState(CallbackQuery callbackQuery);

    public abstract void processBusinessLogic(CallbackQuery callbackQuery);

    public abstract void mappingOutputParameter(CallbackQuery callbackQuery);

    public final void handleCallback(CallbackQuery callbackQuery) {
        mapInputParameters(callbackQuery);
        saveState(callbackQuery);
        processBusinessLogic(callbackQuery);
        mappingOutputParameter(callbackQuery);
    }
}

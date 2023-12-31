package tg.bot.activity.common;

import org.springframework.stereotype.Service;
import tg.bot.activity.service.callbackquery.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallbackMap {
    private static final Map<String, Callback> CALLBACK_MAP = new HashMap<>();

    public CallbackMap(List<Callback> callbacks) {
        callbacks.forEach(callback -> CALLBACK_MAP.put(callback.getSupportedActivities().toString(), callback));
    }

    public Callback getCallback(String keyCallback) {
        return CALLBACK_MAP.get(keyCallback);
    }
}

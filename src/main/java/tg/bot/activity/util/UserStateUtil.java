package tg.bot.activity.util;

import lombok.experimental.UtilityClass;
import tg.bot.activity.model.UserState;

@UtilityClass
public class UserStateUtil {

    public static UserState getUserState(Long adminTelegramId, Enum<?> state, Object entity, boolean forUpdate) {
        UserState userState = new UserState();
        userState.setAdminChatId(adminTelegramId);
        userState.setState(state);
        userState.setEntity(entity);
        userState.setForUpdate(forUpdate);

        return userState;
    }
}

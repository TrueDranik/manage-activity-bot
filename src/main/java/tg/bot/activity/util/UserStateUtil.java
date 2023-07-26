package tg.bot.activity.util;

import com.bot.sup.model.UserState;
import lombok.experimental.UtilityClass;

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

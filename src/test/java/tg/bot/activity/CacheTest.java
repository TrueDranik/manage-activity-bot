package tg.bot.activity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import tg.bot.activity.cache.UserStateCache;
import tg.bot.activity.common.enums.states.InstructorStateEnum;
import tg.bot.activity.model.UserState;
import tg.bot.activity.model.entity.Instructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@Profile("master")
class CacheTest extends AbstractTest {

    @Autowired
    UserStateCache stateCache;

    @Test
    void testCache() {
        Object instructor = new Instructor();
        UserState userState = new UserState();
        userState.setAdminChatId(432423432L);
        userState.setEntity(instructor);
        userState.setState(InstructorStateEnum.FILLING_INSTRUCTOR);

        stateCache.createOrUpdateState(userState);
        UserState resultUserState = stateCache.getByChatId(432423432L);
        assertEquals(userState, resultUserState);
        log.info("Got user - {}", resultUserState);

        stateCache.deleteFromCache(432423432L);
        UserState resultUserState2 = stateCache.getByChatId(432423432L);
        assertNull(resultUserState2);
        log.info("Got user - {}", resultUserState2);
    }
}

package tg.bot.activity.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tg.bot.activity.model.UserState;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateCache {

    private final CacheManager cacheManager;

    @Cacheable(value = "states")
    public UserState getByChatId(Long chatId) {
        Cache states = cacheManager.getCache("states");
        return (UserState) Optional.ofNullable(states).map(it -> it.get(chatId)).map(Cache.ValueWrapper::get)
                .orElseThrow(() -> new IllegalArgumentException("Cache with tgId - %s not found".formatted(chatId)));
    }

    @CachePut(value = "states", key = "#state.adminChatId")
    public UserState createOrUpdateState(UserState state) {
        log.info("Saving user state with values - [{}:{}]", state.getState(), state.getAdminChatId());
        return state;
    }

    @CacheEvict(value = "states", key = "#chatId")
    public void deleteFromCache(Long chatId) {
        log.info("Deleting user state with tgId - [{}]", chatId);
    }
}

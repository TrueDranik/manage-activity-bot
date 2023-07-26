package tg.bot.activity.model;

import lombok.Data;

@Data
public class UserState {
    private Long adminChatId;
    private Enum<?> state;
    private Object entity;
    private boolean forUpdate;
}

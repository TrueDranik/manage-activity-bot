package tg.bot.activity.model.dto.vk;

import lombok.Getter;
import lombok.Setter;
import tg.bot.activity.common.enums.CommentStatusEnum;

import java.time.LocalDateTime;

@Getter
@Setter
public class VkCommentDto {

    private long id;
    private int vkId;
    private String text;
    private CommentStatusEnum status;
    private LocalDateTime date;
    private long userId;
    private long postId;
}

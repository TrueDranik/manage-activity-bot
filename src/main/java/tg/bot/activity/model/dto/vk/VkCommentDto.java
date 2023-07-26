package tg.bot.activity.model.dto.vk;

import com.bot.sup.common.enums.CommentStatusEnum;
import lombok.Getter;
import lombok.Setter;

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

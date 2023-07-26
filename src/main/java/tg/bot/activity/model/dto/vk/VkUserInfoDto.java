package tg.bot.activity.model.dto.vk;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
public class VkUserInfoDto {
    private long id;
    private int vkId;
    private String firstName;
    private String lastName;
    private URI photoSize50;
}

package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Long telegramId;
    private String username;
    private String phoneNumber;
}

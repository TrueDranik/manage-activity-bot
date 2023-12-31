package tg.bot.activity.model.dto.tg;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Long telegramId;
    private String username;
    private String phoneNumber;
}

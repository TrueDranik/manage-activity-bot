package tg.bot.activity.common.properties.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import tg.bot.activity.common.properties.YamlPropertySourceFactory;

@Data
@ConfigurationProperties(prefix = "message.instructor")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class InstructorMessageProperties {

    String instructors;
    String menuInstructors;
    String listInstructor;
    String addInstructor;
    String deleteInstructor;
    String emptyInstructors;
    String inputFullNameInstructor;
    String inputFullNameInstructorIsEmpty;
    String validateInputFullName;
    String validateLanguage;
    String inputPhoneNumber;
    String getTelegramId;
    String phoneNumberNotValid;
    String telegramIdAlreadyTaken;
    String telegramIdIsEmpty;
    String registrationDone;
    String getPhoto;
}

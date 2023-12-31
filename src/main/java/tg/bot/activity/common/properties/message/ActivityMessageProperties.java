package tg.bot.activity.common.properties.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tg.bot.activity.common.properties.YamlPropertySourceFactory;

@Configuration
@Data
@ConfigurationProperties(prefix = "message.activity")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class ActivityMessageProperties {

    String activities;
    String activityFormat;
    String activityType;
    String menuActivities;
    String listActivityFormat;
    String listActivityType;
    String addActivityFormat;
    String addActivityType;
    String deleteActivity;
    String registeredActivity;
    String emptyActivity;
    String inputActivityFormatOrTypeName;
    String inputActivityNameIsEmpty;
    String activityNameAlreadyTaken;
    String sendPhoto;
    String notValidIconFormat;
    String updatedActivity;
    String sendFormatDescription;
    String formatDescriptionTooLong;
    String chooseFormatToBind;
    String activityNameTooLong;
    String activityNameWarning;
}

package tg.bot.activity.common.properties.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tg.bot.activity.common.properties.YamlPropertySourceFactory;

@Configuration
@Data
@ConfigurationProperties(prefix = "message.main")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class MainMessageProperties {

    String menu;
    String change;
    String delete;
    String back;
    String userChoose;
    String done;
    String commandStart;
    String commandStartDescription;
    String gallery;
    String aboutUs;
    String reviews;
    String infoAboutPayment;
}

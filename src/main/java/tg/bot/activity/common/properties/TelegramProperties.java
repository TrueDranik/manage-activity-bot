package tg.bot.activity.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private String botUsername;
    @Value("${telegram.botToken}")
    private String botToken;
    @Value("${telegram.url.constructor}")
    private String webAppConstructor;
    @Value("${telegram.url.activity}")
    private String webAppActivity;
    @Value("${telegram.url.updateActivity}")
    private String webAppUpdateActivity;
    @Value("${telegram.url.route}")
    private String webAppRoute;
    @Value("${telegram.url.updateRoute}")
    private String webAppUpdateRoute;
    @Value("${telegram.url.updateSchedule}")
    private String updateSchedule;
    @Value("${telegram.url.getClientRecords}")
    private String webAppClientRecords;
    @Value("${telegram.url.getGallery}")
    private String webAppGallery;
    @Value("${telegram.url.aboutUs}")
    private String webAppAboutUs;
    @Value("${telegram.url.reviews}")
    private String webAppReviews;
    @Value("${telegram.url.infoAboutPayment}")
    private String webAppInfoAboutPayment;
}

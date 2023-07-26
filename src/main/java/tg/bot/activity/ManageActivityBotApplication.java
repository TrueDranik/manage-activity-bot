package tg.bot.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan("tg.bot.activity.common.properties")
@EnableCaching
@EnableFeignClients
public class ManageActivityBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageActivityBotApplication.class, args);
    }

}

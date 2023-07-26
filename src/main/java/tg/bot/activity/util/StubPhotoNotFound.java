package tg.bot.activity.util;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class StubPhotoNotFound {
    private InputStream imageDataToSent = this.getClass().getClassLoader().getResourceAsStream("map.jpg");
    private String imageName = "map.jpg";
}

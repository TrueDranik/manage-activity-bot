package tg.bot.activity.repository.converter;

import com.bot.sup.common.enums.PhotoSizeEnum;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class PhotoSizeEnumConverter implements Converter<String, PhotoSizeEnum> {
    @Override
    public PhotoSizeEnum convert(String source) {
        return PhotoSizeEnum.valueOf(source.toUpperCase());
    }
}

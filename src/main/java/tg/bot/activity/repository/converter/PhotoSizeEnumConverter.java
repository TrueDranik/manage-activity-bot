package tg.bot.activity.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tg.bot.activity.common.enums.PhotoSizeEnum;

@Component
public class PhotoSizeEnumConverter implements Converter<String, PhotoSizeEnum> {

    @Override
    public PhotoSizeEnum convert(String source) {
        return PhotoSizeEnum.valueOf(source.toUpperCase());
    }
}

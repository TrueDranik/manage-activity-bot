package tg.bot.activity.service;

import org.springframework.stereotype.Component;
import tg.bot.activity.common.enums.PhotoSizeEnum;

import java.util.EnumMap;
import java.util.List;

@Component
public class PhotoFactory {

    protected static final EnumMap<PhotoSizeEnum, PhotoHandler> PHOTO_HANDLER_MAP = new EnumMap<>(PhotoSizeEnum.class);

    PhotoFactory(List<PhotoHandler> photoHandlers) {
        photoHandlers.forEach(p -> PHOTO_HANDLER_MAP.put(p.getSize(), p));
    }

    public static  PhotoHandler getPhotoHandler(PhotoSizeEnum size) {
        return PHOTO_HANDLER_MAP.get(size);
    }
}

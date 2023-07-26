package tg.bot.activity.service;

import com.bot.sup.common.enums.PhotoSizeEnum;

public interface PhotoHandler {
    String getPhotoUrlFromDb(Long id);

    PhotoSizeEnum getSize();
}

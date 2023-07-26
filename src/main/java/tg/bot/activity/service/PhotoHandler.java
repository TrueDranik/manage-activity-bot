package tg.bot.activity.service;

import tg.bot.activity.common.enums.PhotoSizeEnum;

public interface PhotoHandler {

    String getPhotoUrlFromDb(Long id);

    PhotoSizeEnum getSize();
}

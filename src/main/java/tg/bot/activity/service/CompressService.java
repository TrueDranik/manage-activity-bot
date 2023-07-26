package tg.bot.activity.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CompressService {

    byte[] tryToScale(MultipartFile fileFromRequest, String fileExtension) throws IOException;
}

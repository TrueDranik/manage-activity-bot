package tg.bot.activity.service;

import com.bot.sup.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class CompressServiceImpl implements CompressService {

    @Override
    public byte[] tryToScale(MultipartFile fileFromRequest, String fileExtension) throws IOException {
        if (isNeedToCompress(fileExtension)) {
            try {
                return ImageUtil.scaleImage(fileFromRequest, fileExtension);
            } catch (IOException e) {
                log.error("Ошибка сжатия изображения", e);
            }
        }
        return fileFromRequest.getBytes();
    }

    private boolean isNeedToCompress(String fileExtension) {
        return !fileExtension.equals("svg");
    }

}

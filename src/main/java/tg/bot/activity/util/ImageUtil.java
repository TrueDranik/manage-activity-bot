package tg.bot.activity.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ImageUtil {
    private ImageUtil() {
        throw new IllegalStateException("Util class");
    }

    public static byte[] uploadFile(String fileId, String botToken) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId);
        String res;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            res = in.readLine();
        }

        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String filePath = path.getString("file_path");

        InputStream download = new URL("https://api.telegram.org/file/bot" + botToken + "/" + filePath).openStream();
        return IOUtils.toByteArray(download);
    }

    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);

            InputStream inStream = conn.getInputStream();
            return readInputStream(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();

        return outStream.toByteArray();
    }

    public static InputFile getPhotoToSend(String url, String name) {
        String imageName;
        InputStream imageDataToSent;
        if (url == null) {
            StubPhotoNotFound stubPhoto = new StubPhotoNotFound();
            imageDataToSent = stubPhoto.getImageDataToSent();
            imageName = stubPhoto.getImageName();

        } else {
            imageDataToSent = new ByteArrayInputStream(getImageFromNetByUrl(url));
            imageName = name;
        }
        return new InputFile(imageDataToSent, imageName);
    }

    public static byte[] uploadPhoto(List<PhotoSize> sentPhotos, AbsSender absSender, String botToken) throws ExecutionException, InterruptedException {
        GetFile getFileRequest = new GetFile();
        int lastIndexPhotos = sentPhotos.size() - 1;
        getFileRequest.setFileId(sentPhotos.get(lastIndexPhotos).getFileId());

        CompletableFuture<File> file = null;
        try {
            file = absSender.executeAsync(getFileRequest);
        } catch (TelegramApiException e) {
            log.error("error download file" + e.getMessage());
        }

        String filePath = file.get().getFileUrl(botToken);
        return ImageUtil.getImageFromNetByUrl(filePath);
    }

    public static byte[] scaleImage(MultipartFile fileFromRequest, String fileExtension) throws IOException {
        BufferedImage srcImage = ImageIO.read(fileFromRequest.getInputStream());
        BufferedImage scaledImage = Scalr.resize(srcImage, 150);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, fileExtension, stream);
        return stream.toByteArray();
    }
}


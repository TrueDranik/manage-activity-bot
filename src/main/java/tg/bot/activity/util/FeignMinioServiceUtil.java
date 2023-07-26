package tg.bot.activity.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "minioService", url = "${integration.url.minioService}")
public interface FeignMinioServiceUtil {
    @GetMapping("buckets/{bucketName}/files/{fileName}/url")
    String getUrlToUploadFile(@PathVariable("fileName") String fileName, @PathVariable("bucketName") String bucketName);

    @PostMapping(value = "buckets/{bucketName}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void handleFileUpload(@PathVariable("bucketName") String bucketName, @RequestPart(value = "file") MultipartFile file);

    @DeleteMapping("buckets/{bucketName}/files/{fileName}")
    String deleteUrlToUploadFile(@PathVariable("fileName") String fileName, @PathVariable("bucketName") String bucketName);
}

package tg.bot.activity.service.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFileConstructor {

    public static MultipartFile getNewFile(String fileName, MultipartFile currentFile) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return currentFile.getName();
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return currentFile.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return currentFile.isEmpty();
            }

            @Override
            public long getSize() {
                return currentFile.getSize();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return currentFile.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return currentFile.getInputStream();
            }

            @Override
            public void transferTo(java.io.File dest) throws IllegalStateException {
            }
        };
    }

    public static MultipartFile getMultipartFileFromByteArray(String fileName, byte[] input) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return input == null || input.length == 0;
            }

            @Override
            public long getSize() {
                return input.length;
            }

            @Override
            public byte[] getBytes() {
                return input;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(input);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {

                try (FileOutputStream fos = new FileOutputStream(dest)) {
                    fos.write(input);
                }
            }
        };
    }
}

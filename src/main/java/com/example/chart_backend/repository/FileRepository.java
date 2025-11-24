package com.example.chart_backend.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.chart_backend.dto.FileInfo;
import com.example.chart_backend.entity.FileMgmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Repository
public class FileRepository {

    @Value("C:/upload")
    String storageDir;

    @Value("http://localhost:8080/api/v1/users/media/download/")
    String urlPrefix;

    public FileInfo store(MultipartFile file) throws IOException {
        Path folder = Paths.get(storageDir);
        Files.createDirectories(folder); // đảm bảo folder tồn tại

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String fileName = Objects.isNull(fileExtension)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID() + "." + fileExtension;

        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        // đọc bytes 1 lần để vừa lưu file, vừa tính md5
        byte[] bytes = file.getBytes();
        Files.write(filePath, bytes);

        String md5 = DigestUtils.md5DigestAsHex(bytes);

        return FileInfo.builder()
                .name(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5Checksum(md5)
                .path(filePath.toString())
                .url(fileName)
                .build();
    }

    public Resource read(FileMgmt fileMgmt) throws IOException {
        var data = Files.readAllBytes(Path.of(fileMgmt.getPath()));
        return new ByteArrayResource(data);
    }

    public void delete(FileMgmt fileMgmt) throws IOException {
        if (fileMgmt == null || fileMgmt.getPath() == null)
            return;
        Files.deleteIfExists(Path.of(fileMgmt.getPath()));
    }
}

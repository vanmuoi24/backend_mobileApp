package com.example.chart_backend.repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.chart_backend.dto.FileInfo;
import com.example.chart_backend.entity.FileMgmt;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final Cloudinary cloudinary;

    public FileInfo store(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", UUID.randomUUID().toString(),
                        "resource_type", "auto" // ảnh / video / pdf
                ));

        String publicId = Objects.toString(uploadResult.get("public_id"), "");
        String secureUrl = Objects.toString(uploadResult.get("secure_url"), "");
        String originalFilename = Objects.toString(uploadResult.get("original_filename"), "unknown");

        return FileInfo.builder()
                .name(originalFilename)
                .size(file.getSize())
                .contentType(file.getContentType())
                .path(publicId) // LƯU public_id ở đây
                .url(secureUrl) // LƯU secure_url để FE dùng trực tiếp
                .build();
    }

    public Resource read(FileMgmt fileMgmt) throws IOException {
        // Nếu vẫn muốn cho download qua BE:
        URL url = new URL(fileMgmt.getUrl()); // đã lưu secure_url ở DB
        byte[] data = url.openStream().readAllBytes();
        return new ByteArrayResource(data);
    }

    public void delete(FileMgmt fileMgmt) {
        try {
            if (fileMgmt == null)
                return;

            String publicId = fileMgmt.getUrl(); // DÙNG PATH (public_id) chứ không parse URL
            if (publicId == null || publicId.isBlank())
                return;

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa file khỏi Cloudinary", e);
        }
    }
}

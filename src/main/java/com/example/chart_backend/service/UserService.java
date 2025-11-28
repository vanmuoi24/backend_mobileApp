package com.example.chart_backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.chart_backend.dto.request.ResCreateUserDTO;
import com.example.chart_backend.dto.response.FileData;
import com.example.chart_backend.dto.response.FileResponse;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.mapper.FileMgmtMapper;
import com.example.chart_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final com.example.chart_backend.repository.FileRepository fileRepository;
    private final com.example.chart_backend.repository.FileMgmtRepository fileMgmtRepository;
    private final FileMgmtMapper fileMgmtMapper;
    public User handleGetUserByUserNawm(String bhxh) {
        return this.userRepository.findByBhxhNumber(bhxh);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        User userUPdate = this.fetchUserById(user.getId());

        if (userUPdate != null) {

            userUPdate.setUserPhone(user.getUserPhone());

        }
        return this.userRepository.save(userUPdate);
    }

    public User fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public void updateUserToken(String refreshToken, String username) {
        User user = handleGetUserByUserNawm(username);
        if (user != null) {
            userRepository.save(user);
        }
    }

    public User createUser(User user) {
        // DB hiện có cột user_email NOT NULL, tự sinh giá trị nội bộ để tránh lỗi
        try {
            var emailField = user.getClass().getDeclaredField("userEmail");
            emailField.setAccessible(true);
            Object val = emailField.get(user);
            if (val == null || val.toString().isBlank()) {
                String fallback = (user.getBhxhNumber() != null && !user.getBhxhNumber().isBlank())
                        ? user.getBhxhNumber() + "@local"
                        : "user@local";
                emailField.set(user, fallback);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setUserFullname(user.getUserFullname());
        resCreateUserDTO.setUserPhone(user.getUserPhone());
        return resCreateUserDTO;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUserFullname(userDetails.getUserFullname());

            user.setUserPhone(userDetails.getUserPhone());
            user.setBhxhNumber(userDetails.getBhxhNumber());
            user.setCitizenId(userDetails.getCitizenId());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setAddress(userDetails.getAddress());
            user.setCardNumber(userDetails.getCardNumber());
            user.setCardIssuedDate(userDetails.getCardIssuedDate());
            user.setCardExpiryDate(userDetails.getCardExpiryDate());
            user.setHospitalRegistered(userDetails.getHospitalRegistered());
            user.setCardStatus(userDetails.getCardStatus());
            return userRepository.save(user);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public FileResponse uploadFile(MultipartFile file, String userId) throws IOException {
        // 1. Upload file lên Cloudinary
        var fileInfo = fileRepository.store(file);

        // 2. Map sang entity FileMgmt
        var fileMgmt = fileMgmtMapper.toFileMgmt(fileInfo);
        fileMgmt.setId(UUID.randomUUID().toString()); // primary key
        fileMgmt.setOwnerId(userId); // nhớ kiểu cột là VARCHAR

        // 3. Tìm user
        var user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 4. Nếu user đã có file cũ thì xoá
        var oldFileOpt = fileMgmtRepository.findByOwnerId(userId);
        if (oldFileOpt.isPresent()) {
            var oldFile = oldFileOpt.get();

            // 4.1. Xoá file cũ trên Cloudinary
            fileRepository.delete(oldFile);

            // 4.2. Xoá bản ghi cũ trong DB
            fileMgmtRepository.delete(oldFile);
        }

        // 5. Cập nhật avatarUrl cho user = secure_url của Cloudinary
        user.setAvatarUrl(fileInfo.getUrl()); // frontend dùng thẳng URL này để hiển thị ảnh
        userRepository.save(user);

        // 6. Lưu metadata file mới
        fileMgmtRepository.save(fileMgmt);

        // 7. Trả về cho FE
        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getUrl()) // secure_url
                .build();
    }

    public FileData download(String fileId) throws IOException {
        var optionalFileMgmt = fileMgmtRepository.findById(fileId);
        if (optionalFileMgmt.isEmpty()) {
            throw new IOException("File not found: " + fileId);
        }
        var fileMgmt = optionalFileMgmt.get();
        var resource = fileRepository.read(fileMgmt);

        return new FileData(fileMgmt.getContentType(), resource);
    }
}

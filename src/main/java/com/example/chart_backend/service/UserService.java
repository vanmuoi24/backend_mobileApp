package com.example.chart_backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Value("${avatar.upload-dir}")
    private String avatarUploadDir;
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
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setUserEmail(user.getUserEmail());
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
            user.setUserEmail(userDetails.getUserEmail());
            user.setUserPhone(userDetails.getUserPhone());
            user.setUserPassword(userDetails.getUserPassword());

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

    public FileResponse uploadAvatar(MultipartFile file, String userId) throws IOException {
        // Nếu muốn lấy từ token thì dùng dòng này, khỏi cần truyền userId từ FE
        // String userId =
        // SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Lấy user


        var user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 2. Tìm avatar cũ của user (nếu có)
        var oldFileOpt = fileMgmtRepository.findByOwnerId(userId);
        if (oldFileOpt.isPresent()) {
            var oldFile = oldFileOpt.get();

            // 2.1. Xoá file cũ trên ổ cứng
            fileRepository.delete(oldFile);

            // 2.2. Xoá bản ghi cũ trong DB
            fileMgmtRepository.delete(oldFile);
        }

        // 3. Lưu file mới vào ổ cứng
        var fileInfo = fileRepository.store(file);

        // 4. Lưu thông tin file vào bảng file_mgmt
        var fileMgmt = fileMgmtMapper.toFileMgmt(fileInfo);
        fileMgmt.setOwnerId(userId);
        fileMgmt = fileMgmtRepository.save(fileMgmt);

        // 5. Cập nhật avatarUrl cho user
        user.setAvatarUrl(fileInfo.getUrl()); // nhớ thêm field này trong entity User
        userRepository.save(user);

        // 6. Trả response cho FE
        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getUrl())
                .build();
    }

    public FileData download(String fileName) throws IOException {
        var optionalFileMgmt = fileMgmtRepository.findById(fileName);
        if (optionalFileMgmt.isEmpty()) {
            throw new IOException("File not found: " + fileName);
        }
        var fileMgmt = optionalFileMgmt.get();
        var resource = fileRepository.read(fileMgmt);
        return new FileData(fileMgmt.getContentType(), resource);
    }

}

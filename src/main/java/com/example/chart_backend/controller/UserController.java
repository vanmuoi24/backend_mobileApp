package com.example.chart_backend.controller;

import com.example.chart_backend.dto.response.ApiResponse;
import com.example.chart_backend.dto.response.FileResponse;
import com.example.chart_backend.entity.User;

import com.example.chart_backend.service.UserService;
import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy danh sách tất cả user
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách người dùng thành công", users));
    }

    // Lấy user theo id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công", user)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Không tìm thấy người dùng")));
    }

    // Thêm mới user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(ApiResponse.success("Tạo người dùng thành công", created));
    }

    // Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(updated -> ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", updated)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Không tìm thấy người dùng để cập nhật")));
    }

    // Xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công", null));
        }
        return ResponseEntity.status(404).body(ApiResponse.error("Không tìm thấy người dùng để xóa"));
    }

    @PostMapping("/media/upload")
    public ResponseEntity<ApiResponse<FileResponse>> uploadMedia(@RequestParam("file") MultipartFile file , 
            @RequestParam("userId") String userId) throws IOException {
        FileResponse fileResponse = userService.uploadAvatar(file , userId);
        return ResponseEntity.ok(ApiResponse.success("Tải lên file thành công", fileResponse));
    }

    @GetMapping("/media/download/{fileName}")
    ResponseEntity<Resource> downloadMedia(@PathVariable String fileName) throws IOException {
        var fileData = userService.download(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.contentType())
                .body(fileData.resource());
    }

}

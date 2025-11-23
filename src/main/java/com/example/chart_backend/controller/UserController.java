package com.example.chart_backend.controller;

import com.example.chart_backend.dto.response.ApiResponse;
import com.example.chart_backend.entity.User;

import com.example.chart_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<User>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("avatar") MultipartFile avatarFile) {

        if (avatarFile.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File avatar không được để trống"));
        }

        return userService.updateUserAvatar(id, avatarFile)
                .map(updatedUser -> ResponseEntity.ok(
                        ApiResponse.success("Cập nhật avatar thành công", updatedUser)))
                .orElse(
                        ResponseEntity.status(404)
                                .body(ApiResponse.error("Không tìm thấy người dùng để cập nhật avatar")));
    }

}

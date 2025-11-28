package com.example.chart_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chart_backend.dto.request.LoginRequest;
import com.example.chart_backend.dto.request.ResCreateUserDTO;
import com.example.chart_backend.dto.response.RestLogin;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.service.UserService;
import com.example.chart_backend.utils.SecurityUntil;
import com.example.chart_backend.utils.error.IdInvalidException;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
      private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUntil securityUntil;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

@PostMapping("/auth/login")
  public ResponseEntity<RestLogin> login(@Valid @RequestBody LoginRequest loginDTO)
    throws IdInvalidException {
    User currentuserDb =
      this.userService.handleGetUserByUserNawm(loginDTO.getBhxhNumber());

    if (currentuserDb == null) {
      throw new IdInvalidException("Người dùng không tồn tại trong hệ thống");
    }

    if (
      !passwordEncoder.matches(
        loginDTO.getPassword(),
        currentuserDb.getUserPassword()
      )
    ) {
      throw new IdInvalidException("Mật khẩu người dùng không đúng");
    }
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      loginDTO.getBhxhNumber(),
      loginDTO.getPassword()
    );

    Authentication authentication = authenticationManagerBuilder
      .getObject()
      .authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    RestLogin restLogin = new RestLogin();

 
    userService.updateUser(currentuserDb);

    RestLogin.UserLogin userLogin = new RestLogin.UserLogin(
      currentuserDb.getId(),

      currentuserDb.getUserFullname()
    );
    restLogin.setUser(userLogin);

    String access_token =
      this.securityUntil.createToken(
          authentication.getName(),
          restLogin.getUser()
        );
    restLogin.setAccessToken(access_token);

   

    // Update user to

    // Set cookie
    ResponseCookie responseCookie = ResponseCookie
      .from("refresh_token")
      .httpOnly(true)
      .secure(true)
      .sameSite("None")
      .path("/")
      .maxAge(60)
      .build();
    return ResponseEntity
      .ok()
      .header(
        org.springframework.http.HttpHeaders.SET_COOKIE,
        responseCookie.toString()
      )
      .body(restLogin);
  }

 // register
  @PostMapping("/auth/register")
  public ResponseEntity<ResCreateUserDTO> userRegister(
    @Valid @RequestBody User bodyUser
  ) throws IdInvalidException {
    User currentDb =
      this.userService.handleGetUserByUserNawm(bodyUser.getBhxhNumber());
    if (currentDb != null) {
      throw new IdInvalidException("Người dùng đã tồn tại trong hệ thống");
    }


    User listUser = this.userService.createUser(bodyUser);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(this.userService.convertToResCreateUserDTO(listUser));
  }
}

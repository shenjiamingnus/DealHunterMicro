package com.nus.dhuser.controller;

import com.nus.common.exception.CommonException;
import com.nus.dhmodel.pojo.User;
import com.nus.dhuser.payload.request.AdminCreateRequest;
import com.nus.dhuser.payload.request.LoginRequest;
import com.nus.dhuser.payload.request.SignupRequest;
import com.nus.dhuser.payload.request.UserEmailModifyRequest;
import com.nus.dhuser.payload.request.UserPasswordModifyRequest;
import com.nus.dhuser.payload.response.GeneralApiResponse;
import com.nus.dhuser.service.UserService;
import com.nus.dhuser.vo.JwtVo;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  @Autowired
  UserService userService;

  @RequestMapping("/getUsers")
  public List<User> getUserByUserId(@RequestBody List<Long> userIds){
    return userService.getUsersByIds(userIds);
  }

  @PostMapping("/login")
  public ResponseEntity<GeneralApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      JwtVo jwtVo = userService.login(loginRequest);
      return ResponseEntity.ok(new GeneralApiResponse(true, "log in successed!", jwtVo));
    } catch (CommonException commonException) {
      return ResponseEntity.ok(new GeneralApiResponse(false, commonException.getMessage()));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<GeneralApiResponse> register(@Valid @RequestBody SignupRequest signupRequest){
    if (userService.checkUserNameExists(signupRequest.getUsername())) {
      return new ResponseEntity<>(new GeneralApiResponse(false, "Username already registered!"), HttpStatus.BAD_REQUEST);
    }
    User user = userService.createUser(signupRequest);
    if (user != null) {
      return ResponseEntity.ok(new GeneralApiResponse(true, "User registered!"));
    }
    return ResponseEntity.ok(new GeneralApiResponse(false, "User register failed."));
  }

  @PutMapping("/modify/password")
  public ResponseEntity<GeneralApiResponse> modifyPassword(@RequestHeader("userId") Long userId, @Valid @RequestBody UserPasswordModifyRequest userModifyRequest) {
    User modifyUser = userService.modifyUserPassword(userId, userModifyRequest);
    if (modifyUser!= null) {
      return ResponseEntity.ok(new GeneralApiResponse(true, "User Detail modified!"));
    }
    return ResponseEntity.ok(new GeneralApiResponse(false, "User Detail modify failed"));
  }

  @PutMapping("/modify/email")
  public ResponseEntity<GeneralApiResponse> modifyEmail(@RequestHeader("userId") Long userId, @Valid @RequestBody UserEmailModifyRequest userModifyRequest) {
    User modifyUser = userService.modifyUserEmail(userId, userModifyRequest);
    if (modifyUser!= null) {
      return ResponseEntity.ok(new GeneralApiResponse(true, "User Detail modified!"));
    }
    return ResponseEntity.ok(new GeneralApiResponse(false, "User Detail modify failed"));
  }

  @PostMapping("/admin/create")
  public ResponseEntity<GeneralApiResponse> createAdmin(@Valid @RequestBody AdminCreateRequest adminCreateRequest, @RequestHeader("isAdmin") int isAdmin) {
    if (isAdmin != 1) {
      return ResponseEntity.ok(new GeneralApiResponse(false,"Not Admin User!"));
    }
    if (userService.checkUserNameExists(adminCreateRequest.getUsername())) {
      return new ResponseEntity<>(new GeneralApiResponse(false, "Username already registered!"), HttpStatus.BAD_REQUEST);
    }
    User createdUser = userService.createAdminUser(adminCreateRequest);
    if (createdUser != null) {
      return ResponseEntity.ok(new GeneralApiResponse(true, "User registered!"));
    }
    return ResponseEntity.ok(new GeneralApiResponse(false, "User register failed."));
  }

}

package com.nus.dhuser;

import static org.mockito.Mockito.when;

import com.nus.dhmodel.pojo.User;
import com.nus.dhuser.controller.UserController;
import com.nus.dhuser.payload.request.AdminCreateRequest;
import com.nus.dhuser.payload.request.LoginRequest;
import com.nus.dhuser.payload.request.SignupRequest;
import com.nus.dhuser.payload.request.UserEmailModifyRequest;
import com.nus.dhuser.payload.request.UserPasswordModifyRequest;
import com.nus.dhuser.service.UserService;
import com.nus.dhuser.vo.JwtVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserControllerTest {

  @Mock
  UserService userService;

  @InjectMocks
  UserController userController;

  @Test
  void login(){
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("string");
    loginRequest.setPassword("string");
    JwtVo jwtVo = new JwtVo();
    jwtVo.setUsername("string");
    when(userService.login(loginRequest)).thenReturn(jwtVo);

    Assertions.assertTrue(userController.login(loginRequest).getBody().getSuccess());
  }

  @Test
  void register() {
    SignupRequest signupRequest1 = new SignupRequest();
    signupRequest1.setUsername("string1");
    SignupRequest signupRequest2 = new SignupRequest();
    signupRequest1.setUsername("string2");

    when(userService.checkUserNameExists(signupRequest1.getUsername())).thenReturn(true);
    Assertions.assertFalse(userController.register(signupRequest1).getBody().getSuccess());
    when(userService.checkUserNameExists(signupRequest2.getUsername())).thenReturn(false);
    when(userService.createUser(signupRequest2)).thenReturn(new User());
    Assertions.assertTrue(userController.register(signupRequest2).getBody().getSuccess());

  }

  @Test
  void modifyPassword() {
    UserPasswordModifyRequest userPasswordModifyRequest1 = new UserPasswordModifyRequest();
    when(userService.modifyUserPassword(1L, userPasswordModifyRequest1)).thenReturn(null);
    Assertions.assertFalse(userController.modifyPassword(1L,userPasswordModifyRequest1).getBody().getSuccess());
    UserPasswordModifyRequest userPasswordModifyRequest2 = new UserPasswordModifyRequest();
    User user = new User();
    when(userService.modifyUserPassword(2L,userPasswordModifyRequest2)).thenReturn(user);
    Assertions.assertTrue(userController.modifyPassword(2L,userPasswordModifyRequest2).getBody().getSuccess());
  }

  @Test
  void modifyEmail() {
    UserEmailModifyRequest userPasswordModifyRequest1 = new UserEmailModifyRequest();
    when(userService.modifyUserEmail(1L, userPasswordModifyRequest1)).thenReturn(null);
    Assertions.assertFalse(userController.modifyEmail(1L, userPasswordModifyRequest1).getBody().getSuccess());
    UserEmailModifyRequest userPasswordModifyRequest2 = new UserEmailModifyRequest();
    User user = new User();
    when(userService.modifyUserEmail(2L, userPasswordModifyRequest2)).thenReturn(user);
    Assertions.assertTrue(userController.modifyEmail(2L, userPasswordModifyRequest2).getBody().getSuccess());
  }

  @Test
  void createAdmin() {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest();
    adminCreateRequest.setUsername("aaa");
    when(userService.checkUserNameExists(adminCreateRequest.getUsername())).thenReturn(true);
    Assertions.assertFalse(userController.createAdmin(adminCreateRequest, 1).getBody().getSuccess());
    when(userService.checkUserNameExists(adminCreateRequest.getUsername())).thenReturn(false);
    when(userService.createAdminUser(adminCreateRequest)).thenReturn(null);
    Assertions.assertFalse(userController.createAdmin(adminCreateRequest, 1).getBody().getSuccess());
    when(userService.createAdminUser(adminCreateRequest)).thenReturn(new User());
    Assertions.assertTrue(userController.createAdmin(adminCreateRequest, 1).getBody().getSuccess());

  }
}
package com.nus.dhuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nus.dhmodel.enums.RoleName;
import com.nus.dhmodel.pojo.Role;
import com.nus.dhmodel.pojo.User;
import com.nus.dhuser.payload.request.AdminCreateRequest;
import com.nus.dhuser.payload.request.SignupRequest;
import com.nus.dhuser.payload.request.UserEmailModifyRequest;
import com.nus.dhuser.payload.request.UserPasswordModifyRequest;
import com.nus.dhuser.repository.RoleRepository;
import com.nus.dhuser.repository.UserRepository;
import com.nus.dhuser.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserServiceTest {

  @Mock
  RoleRepository roleRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Test
  void checkUserNameExists() {
    when(userRepository.existsByUsername("aaa")).thenReturn(true);
    Assertions.assertTrue(userService.checkUserNameExists("aaa"));
  }

  @Test
  void getUser() {
    when(userRepository.findByUsername("aaa")).thenReturn(Optional.of(new User()));
    Assertions.assertNotNull(userService.getUser("aaa"));
  }

  @Test
  void testGetUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
    Assertions.assertNotNull(userService.getUser(1L));
  }

  @Test
  void createUser() {
    when(passwordEncoder.encode(anyString())).thenReturn("aaa");
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setUsername("aaa");
    signupRequest.setPassword("aaa");
    when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(new Role()));
    when(userRepository.save(any(User.class))).thenReturn(new User());
    Assertions.assertNotNull(userService.createUser(signupRequest));
  }

  @Test
  void createAdminUser() {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest();
    adminCreateRequest.setUsername("aaa");
    adminCreateRequest.setPassword("aaa");
    adminCreateRequest.setEmail("aaa");
    when(passwordEncoder.encode(anyString())).thenReturn("aaa");
    when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(new Role()));
    when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(new Role()));
    when(userRepository.save(any(User.class))).thenReturn(new User());
    Assertions.assertNotNull(userService.createAdminUser(adminCreateRequest));
  }

  @Test
  void modifyUserPassword() {
    User user = new User();
    user.setUsername("aaaa");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(new User());

    UserPasswordModifyRequest userPasswordModifyRequest = new UserPasswordModifyRequest();
    userPasswordModifyRequest.setPassword("aaa");
    Assertions.assertNotNull(userService.modifyUserPassword(1L, userPasswordModifyRequest));
  }

  @Test
  void modifyUserEmail() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
    when(userRepository.save(any(User.class))).thenReturn(new User());
    UserEmailModifyRequest userEmailModifyRequest = new UserEmailModifyRequest();
    userEmailModifyRequest.setEmail("aaa");
    Assertions.assertNotNull(userService.modifyUserEmail(1L, userEmailModifyRequest));

  }
}

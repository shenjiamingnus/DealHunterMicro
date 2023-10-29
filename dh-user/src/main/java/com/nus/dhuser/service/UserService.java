package com.nus.dhuser.service;

import com.nus.common.constant.JwtConstant;
import com.nus.common.exception.CommonException;
import com.nus.dhmodel.enums.RoleName;
import com.nus.dhmodel.pojo.Role;
import com.nus.dhmodel.pojo.User;
import com.nus.dhuser.payload.request.AdminCreateRequest;
import com.nus.dhuser.payload.request.LoginRequest;
import com.nus.dhuser.payload.request.SignupRequest;
import com.nus.dhuser.payload.request.UserEmailModifyRequest;
import com.nus.dhuser.payload.request.UserPasswordModifyRequest;
import com.nus.dhuser.repository.RoleRepository;
import com.nus.dhuser.repository.UserRepository;
import com.nus.dhuser.utils.JwtUtil;
import com.nus.dhuser.utils.UserUtil;
import com.nus.dhuser.vo.JwtVo;
import com.nus.dhuser.vo.UserVO;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public Boolean checkUserNameExists(String username) {
    return userRepository.existsByUsername(username);
  }

  public User getUser(String username) throws CommonException{
    return userRepository.findByUsername(username).orElseThrow(() ->
        new CommonException("User not found!"));
  }

  public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow(() ->
        new CommonException("User not found!"));
  }

  public User createUser(SignupRequest signupRequest) {
    User user = new User(signupRequest.getUsername(), signupRequest.getPassword());
    user.setPassword(UserUtil.getUserEncryptPassword(user.getUsername(),user.getPassword()));
    user.setCreateDate(Instant.now());
    user.setEmail(signupRequest.getEmail());
    Role userRole = roleRepository.findByName(RoleName.USER)
        .orElseThrow(() -> new CommonException("User Role not set."));
    user.setRoles(Collections.singleton(userRole));
    return userRepository.save(user);
  }

  public User createAdminUser(AdminCreateRequest adminCreateRequest) {
    User user = new User(adminCreateRequest.getUsername(), adminCreateRequest.getPassword());
    user.setPassword(UserUtil.getUserEncryptPassword(user.getUsername(), user.getPassword()));
    user.setCreateDate(Instant.now());
    Role userRole = roleRepository.findByName(RoleName.USER)
        .orElseThrow(() -> new CommonException("User Role not set."));
    Role adminRole = roleRepository.findByName(RoleName.ADMIN)
        .orElseThrow(() -> new CommonException("User Role not set."));
    Set<Role> roleSet = new HashSet<>();
    roleSet.add(userRole);
    roleSet.add(adminRole);
    user.setRoles(roleSet);
    return userRepository.save(user);
  }

  public User modifyUserPassword(Long userId, @Valid UserPasswordModifyRequest userModifyRequest) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new CommonException("User not found!"));
    user.setPassword(UserUtil.getUserEncryptPassword(user.getUsername(), userModifyRequest.getPassword()));
    return userRepository.save(user);
  }

  public User modifyUserEmail(Long userId, UserEmailModifyRequest userEmailModifyRequest) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new CommonException("User not found!"));
    user.setEmail(userEmailModifyRequest.getEmail());
    return userRepository.save(user);
  }


  public JwtVo login(LoginRequest loginRequest) throws CommonException {
    User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
        new CommonException("User not found!"));
    if (user.getPassword().equals(UserUtil.getUserEncryptPassword(loginRequest.getUsername(), loginRequest.getPassword()))) {
      UserVO userVO = new UserVO();
      BeanUtils.copyProperties(user, userVO);

      Set<Role> roles = user.getRoles();
      int isAdmin = 0;
      for(Role r : roles) {
        if (r.getName() == RoleName.ADMIN) {
          isAdmin = 1;
          break;
        }
      }
      userVO.setIsAdmin(isAdmin);
      String buildJwt = JwtUtil.buildJwt(this.getLoginExpre(), userVO);
      JwtVo jwtVo = new JwtVo();
      jwtVo.setToken(buildJwt);
      jwtVo.setUsername(userVO.getUsername());
      jwtVo.setId(userVO.getId());
      jwtVo.setIsAdmin(isAdmin);
      return jwtVo;
    }else {
      throw new CommonException("Wrong Password!");
    }
  }

  private Date getLoginExpre(){
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, JwtConstant.EXPIRE_DAY);
    return calendar.getTime();
  }

  public List<User> getUsersByIds(List<Long> userIds) {
    return userRepository.findAllById(userIds);
  }
}

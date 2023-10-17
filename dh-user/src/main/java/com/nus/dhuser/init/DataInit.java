package com.nus.dhuser.init;


import com.nus.dhmodel.enums.RoleName;
import com.nus.dhmodel.pojo.Role;
import com.nus.dhuser.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

  @Autowired
  RoleRepository roleRepository;

  @Override
  public void run(String... args) {
    if (roleRepository.count() == 0) {
      Role admin = new Role();
      admin.setName(RoleName.ADMIN);
      roleRepository.save(admin);
      Role user = new Role();
      user.setName(RoleName.USER);
      roleRepository.save(user);
    }
  }
}

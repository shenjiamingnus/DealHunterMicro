package com.nus.dhuser.repository;


import com.nus.dhmodel.enums.RoleName;
import com.nus.dhmodel.pojo.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName roleName);

}

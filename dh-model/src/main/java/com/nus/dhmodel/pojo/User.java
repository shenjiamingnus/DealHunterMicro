package com.nus.dhmodel.pojo;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})
})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  private String username;

  @NotBlank
  @Size(max = 100)
  private String password;

  private String email;

  @CreatedDate
  private Instant createDate;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();


  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User() {}
}

package com.nus.dhproduct.feign;

import com.nus.dhmodel.pojo.User;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("dh-user")
public interface UserFeignService {

  @RequestMapping("api/user/getUsers")
  List<User> getUserById(@RequestBody List<Long> userIds);

}

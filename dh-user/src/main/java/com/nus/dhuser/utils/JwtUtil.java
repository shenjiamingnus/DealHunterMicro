package com.nus.dhuser.utils;


import com.nus.common.constant.JwtConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import com.nus.dhuser.vo.UserVO;

/**
 * JWT工具
 * @author: zealon
 * @since: 2020/4/14
 */
public class JwtUtil {

    /**
     * 构建JWT对象
     * @param expire
     * @param user
     * @return
     */
    public static String buildJwt(Date expire, UserVO user) {
        String jwt = Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, JwtConstant.SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                .claim("username",user.getUsername())
                .claim("id",user.getId())
                .claim("isAdmin", user.getIsAdmin())
                .compact();
        return jwt;
    }
}

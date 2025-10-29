package com.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {
 /*
 *设置生成jwt令牌
  */
    @Test
    public void testGenerateJWT(){
        //自定义信息
        Map<String,Object> claims = new HashMap<>();
        claims.put("id","1");
        claims.put("username","admin");

        //生成jwt令牌
        String jwt = Jwts.builder()

                .signWith(SignatureAlgorithm.HS512,"tlias")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))//设置令牌有效期7天
                .compact();
        System.out.println("jwt:"+jwt);

        //解析jwt令牌
        Claims claims1 = Jwts.parser().setSigningKey("tlias").parseClaimsJws(jwt).getBody();
        System.out.println("解析后："+claims1);
    }
}

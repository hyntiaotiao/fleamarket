package club.devhub.fleamarket.utils;

import club.devhub.fleamarket.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    public String secret;

    @Value("${jwt.expiration}")
    public int expiration;

    /**
     * 根据user信息生成token
     */
    public String getTokenFromUser(User user) {
        Map<String, String> map = new HashMap<>();
        //jwt中包含两个信息：一个是id，一个是role
        map.put("id", user.getUserId().toString());
        map.put("role", user.getRole().toString());

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, expiration);

        System.out.println(secret);
        return JWT.create()
                .withClaim("user", map)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
    }


    /**
     * 根据token还原为user信息
     * 提示:解析失败会抛JWTVerificationException，请特别注意
     * 准确地说,不会直接抛JWTVerificationException,而是抛出JWTVerificationException的子类,
     * 例如JWTDecodeException,TokenExpiredException等等,它们都继承自JWTVerificationException
     */
    public User getUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        Map<String, Object> map = decodedJWT.getClaim("user").asMap();
        //从jwt中获取的user也包含了id和role
        User user = new User();
        user.setUserId(Long.parseLong(map.get("id").toString()));
        user.setRole(Integer.parseInt(map.get("role").toString()));
        return user;
    }

   /* public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        User user=new User();
        user.setUserId(100l);
        user.setRole(0);
        user.setUsername("132132");
        user.setPassword("56132");
        System.out.println(jwtUtil.getTokenFromUser(user));
        *//*
        String token="";
        System.out.println(jwtUtil.getUserFromToken(token));*//*
    }*/


}
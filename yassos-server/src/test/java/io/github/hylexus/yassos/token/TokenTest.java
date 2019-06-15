package io.github.hylexus.yassos.token;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;

/**
 * @author hylexus
 * Created At 2019-06-14 21:32
 */
public class TokenTest {

    private String secretKey = "123456789012345678901234567890123456789012345678901234567890";

    /**
     * iss (issuer)：签发人
     * exp (expiration time)：过期时间
     * sub (subject)：主题
     * aud (audience)：受众
     * nbf (Not Before)：生效时间
     * iat (Issued At)：签发时间
     * jti (JWT ID)：编号
     */
    @Test
    public void test1() {
        Date exp = new Date(new Date().getTime() + 1000 * 60 * 30L);
        String token = Jwts.builder()
                .setIssuer("yassos")
                .setIssuedAt(new Date())
                .setSubject("user-01")
                .setExpiration(exp)
                .setAudience("normal-user")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        System.out.println(token);
        Jwt jwt = Jwts.parser().setSigningKey(secretKey)
                .parse(token);
        System.out.println(JSON.toJSONString(jwt, true));

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        System.out.println(JSON.toJSONString(claimsJws, true));
    }
}

package com.syc.suda.util.jwt

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts

import static io.jsonwebtoken.SignatureAlgorithm.HS512


@Slf4j
@Component
@CompileStatic
@EnableConfigurationProperties(JwtTokenEntity.class)
class JwtTokenHelper {

    private final JwtTokenEntity jwtToken

    @Autowired
    JwtTokenHelper(JwtTokenEntity jwtToken) {
        this.jwtToken = jwtToken
    }

    String create(String identity) {
        try {
            return Jwts.builder()
                    .setIssuer(jwtToken.getAppName())
                    .setSubject(identity)
                    .setAudience('QA')
                    .setIssuedAt(new Date())
                    .setExpiration(generateExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, jwtToken.getSecret())
                    .compact()
        } catch (Exception e) {
            log.error("Generate jwt token failed:", e);
        }
        return null
    }

    /**
     * 生成一个新的token过期时间
     */
    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + getExpiredIn() * 1000L)
    }

    /**
     * 根据token获得其过期时长
     */
    long getExpiredIn() {
        return jwtToken.getExpInSeconds()
    }

    Claims getAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtToken.getSecret()).parseClaimsJws(token).getBody()
        } catch (Exception ignore) {
        }
        return null
    }

    JwtTokenEntity getJwtTokenEntity() {
        return jwtToken
    }

    // 刷新token
    String refresh(String token) {
        final Claims claims = this.getAllClaims(token);
        if (claims == null) {
            return token
        }
        claims.setIssuedAt(new Date())
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(HS512, jwtToken.getSecret())
                .compact()
    }
}

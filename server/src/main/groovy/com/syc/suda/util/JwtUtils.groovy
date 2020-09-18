package com.syc.suda.util

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.util.jwt.CookieUtils
import com.syc.suda.util.jwt.JwtTokenHelper
import groovy.transform.CompileStatic
import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class JwtUtils {

    void removeAccessToken() {
        CookieUtils.clean(Constants.TOKEN_KEY)
    }

    @Autowired
    private JwtTokenHelper tokenHelper

    String sign(SessionInfo sessionInfo) {
        String token = tokenHelper.create(JsonUtil.toString(sessionInfo))
        if (!token)
            throw new Exception('创建token失败')
        return token
    }

    void saveAccessToken(String token) {
        CookieUtils.setRaw(Constants.TOKEN_KEY, token, this.tokenHelper.getExpiredIn().intValue(), true)
    }

    SessionInfo decode(String token) {
        try {
            Claims claims = tokenHelper.getAllClaims(token)
            String payload = claims.getSubject()
            return JsonUtil.parse(payload, SessionInfo.class)
        } catch (Exception e) {
            // TODO
            // throw new UniversalException(ResponseCodeEnum.TOKEN_EXPIRED)
            throw new Exception('token过期')
        }
    }

    boolean isValid(String token) {
        try {
            Claims claims = tokenHelper.getAllClaims(token)
            return claims != null
        } catch (Exception ignored) {
            return false
        }
    }

    String refresh(String token) {
        if (tokenHelper.getJwtTokenEntity().isAutoRefresh())
            return this.tokenHelper.refresh(token)
        else
            return token
    }
}

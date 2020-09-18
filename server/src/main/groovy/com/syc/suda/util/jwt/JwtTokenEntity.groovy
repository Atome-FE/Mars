package com.syc.suda.util.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token.jwt")
class JwtTokenEntity implements Serializable {

    String appName

    String secret

    Long expInSeconds

    Long mobileExpInSeconds

    String deviceType

    boolean autoRefresh = true

}

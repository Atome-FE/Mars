package com.syc.suda.dto

import com.syc.suda.enums.AvailabilityStatusEnum
import com.syc.suda.model.CommonAuthority
import com.syc.suda.model.CommonRole
import com.syc.suda.model.CommonUserDetails
import groovy.transform.CompileStatic

@CompileStatic
class BackendUserDTO implements Serializable {
    private static final long serialVersionUID = 9002639640245959638L

    String id
    String username
    String email
    String realName
    String mobile
    AvailabilityStatusEnum status
    List<CommonRole> roles
    List<CommonAuthority> authorities

    BackendUserDTO(CommonUserDetails other) {
        this.id = other.id
        this.username = other.username
        this.email = other.email
        this.realName = other.realName
        this.mobile = other.mobile
        this.status = other.status
        this.roles = other.roles
        this.authorities = other.authorities
    }
}

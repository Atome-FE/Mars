package com.syc.suda.model

import com.syc.suda.enums.AvailabilityStatusEnum
import groovy.transform.CompileStatic

@CompileStatic
class CommonAdmin implements Serializable {
    private static final long serialVersionUID = 6323029393867440570L
    String id
    String username
    String password
    String email
    String realName
    String mobile
    AvailabilityStatusEnum status
    Long createTimestamp
    Long updateTimestamp
}

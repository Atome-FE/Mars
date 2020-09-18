package com.syc.suda.model

import com.syc.suda.enums.AvailabilityStatusEnum
import groovy.transform.CompileStatic

@CompileStatic
class CommonRole implements Serializable {
    private static final long serialVersionUID = 6646148390818260548L
    Long id
    String name
    AvailabilityStatusEnum status
    String description
}

package com.syc.suda.exception

import groovy.transform.CompileStatic

@CompileStatic
class BizException extends RuntimeException {

    BizException() {
        super()
    }

    BizException(String message) {
        super(message)
    }

    BizException(String message, Throwable cause) {
        super(message, cause)
    }


}

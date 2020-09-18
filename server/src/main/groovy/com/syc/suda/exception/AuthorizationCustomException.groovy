package com.syc.suda.exception

import groovy.transform.CompileStatic

@CompileStatic
class AuthorizationCustomException extends RuntimeException {
    AuthorizationCustomException() {
    }

    AuthorizationCustomException(String message) {
        super(message)
    }

    AuthorizationCustomException(String message, Throwable cause) {
        super(message, cause)
    }

    AuthorizationCustomException(Throwable cause) {
        super(cause)
    }

    AuthorizationCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
    }
}

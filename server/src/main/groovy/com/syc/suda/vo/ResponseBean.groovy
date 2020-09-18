package com.syc.suda.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.syc.suda.enums.ResponseCodeEnum
import groovy.transform.CompileStatic

@CompileStatic
class ResponseBean {
    String code
    String message
    Object data

    @JsonIgnore
    boolean isSuccess() {
        return code == ResponseCodeEnum.SUCCESS.getCode()
    }

    static ResponseBean success() {
        return new ResponseBean(code: ResponseCodeEnum.SUCCESS.getCode(), message: ResponseCodeEnum.SUCCESS.message)
    }

    static ResponseBean success(Object data) {
        return new ResponseBean(code: ResponseCodeEnum.SUCCESS.getCode(), message: ResponseCodeEnum.SUCCESS.message, data: data)
    }

    static ResponseBean error() {
        return new ResponseBean(code: ResponseCodeEnum.ERROR.getCode(), message: ResponseCodeEnum.ERROR.message)
    }

    static ResponseBean error(String message) {
        return new ResponseBean(code: ResponseCodeEnum.ERROR.getCode(), message: message)
    }

    static ResponseBean fail(ResponseCodeEnum responseCode) {
        return new ResponseBean(code: responseCode.getCode(), message: responseCode.getMessage())
    }

    static ResponseBean fail(ResponseCodeEnum responseCode, String message) {
        return new ResponseBean(code: responseCode.getCode(), message: message)
    }

    static ResponseBean fail(ResponseCodeEnum responseCode, String message, Object data) {
        return new ResponseBean(code: responseCode.getCode(), message: message, data: data)
    }
}

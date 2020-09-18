package com.syc.suda.config

import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.exception.BizException
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@Slf4j
@CompileStatic
class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BizException)
    ResponseEntity<ResponseBean> handleBizException(BizException bizException, HandlerMethod handlerMethod) {
        log.info("BizException caught of method with name [${handlerMethod.method.name}]. Message: [${bizException.message}]")
        return ResponseEntity.ok(ResponseBean.fail(ResponseCodeEnum.ERROR, bizException.message))
    }

    @ExceptionHandler(RuntimeException)
    ResponseEntity<ResponseBean> handleRuntimeException(RuntimeException bizException, HandlerMethod handlerMethod) {
        log.info("RuntimeException caught of method with name [${handlerMethod.method.name}]. Message: [${bizException.message}]")
        return ResponseEntity.ok(ResponseBean.fail(ResponseCodeEnum.ERROR, bizException.message))
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (status != HttpStatus.INTERNAL_SERVER_ERROR) {
            log.warn("Exception handled by default handler. Message: [${e.message}]")
            return super.handleExceptionInternal(e, body, headers, status, request)
        }
        log.error("Uncaught exception: ${e.message}", e)
        return ResponseEntity.ok(ResponseBean.fail(ResponseCodeEnum.ERROR, e.message, e)) as ResponseEntity<Object>
    }
}

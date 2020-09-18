package com.syc.suda.controller

import com.syc.suda.constant.Constants
import com.syc.suda.dto.SessionInfo
import com.syc.suda.entity.TestCase
import com.syc.suda.service.TestCaseService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/test-case-material')
class TestCaseController {

    @Autowired
    TestCaseService testCaseService

    @GetMapping
    ResponseBean listTestCase(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo) {
        log.info("请求测试用例列表")
        return ResponseBean.success(testCaseService.listTestCase(sessionInfo.id))
    }

    @PostMapping
    ResponseBean save(@RequestAttribute(Constants.SESSION_KEY) SessionInfo sessionInfo,
                      @RequestBody TestCase testCase) {
        log.info("创建测试用例")
        testCase.userId = sessionInfo.id
        testCaseService.save(testCase)
        return ResponseBean.success(testCase)
    }

    @PutMapping
    ResponseBean update(@RequestBody TestCase testCase) {
        testCaseService.update(testCase)
        return ResponseBean.success(testCase)
    }

    @DeleteMapping
    ResponseBean deleteById(@RequestBody TestCase testCase) {
        testCaseService.deleteById(testCase.id)
        return ResponseBean.success()
    }
}

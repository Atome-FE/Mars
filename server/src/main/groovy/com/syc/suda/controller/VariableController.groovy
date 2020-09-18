package com.syc.suda.controller

import com.syc.suda.service.VariableService
import com.syc.suda.vo.ResponseBean
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping
class VariableController {

    @Autowired
    VariableService variableService

    @GetMapping("/variable/sleep")
    ResponseBean getAutoTestSleep() {
        HashMap<String, String> result = new HashMap();
        result.put("autoTestSleep", variableService.getValueByName("autoTestSleep"))
        return ResponseBean.success(result)
    }

    @PostMapping("/variable/sleep")
    ResponseBean setAutoTestSleep(@RequestParam("newValue") String newValue) {
        log.info(newValue)
        variableService.updateValue("autoTestSleep", newValue);
        return ResponseBean.success()
    }
}

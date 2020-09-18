package com.syc.suda.cleaner.controller

import com.syc.suda.cleaner.service.CleanerService
import com.syc.suda.vo.ResponseBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/cleaner')
class CleanerController {
    @Autowired
    CleanerService cleanerService
    @PostMapping
    ResponseBean cleanerToNestedMaterial() {
        System.out.println('hello wrold')
        cleanerService.cleanerToNestedMaterial()
        return ResponseBean.success()
    }
}

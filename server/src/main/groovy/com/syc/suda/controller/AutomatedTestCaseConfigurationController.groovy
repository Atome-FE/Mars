package com.syc.suda.controller

import com.syc.suda.dto.AutomatedTestCaseConfigurationDTO
import com.syc.suda.entity.AutoTestCaseConfiguration

import com.syc.suda.service.AutoTestCaseConfigurationService
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


@RestController
@RequestMapping("/testcase/configuration")
@CompileStatic
class AutomatedTestCaseConfigurationController {
    final ExecutorService executorService = new ThreadPoolExecutor(3, 5, 240, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20));


    @Autowired
    AutoTestCaseConfigurationService autoTestCaseConfigurationService;


    @PostMapping("/add")
    ResponseBean addNewTestCase(@RequestBody AutomatedTestCaseConfigurationDTO automatedTestCaseConfiguration){
        AutoTestCaseConfiguration autoTestCaseConfiguration = AutomatedTestCaseConfigurationDTO.convert(automatedTestCaseConfiguration);
        autoTestCaseConfigurationService.addNewAutoTestCaseConfiguration(autoTestCaseConfiguration);
        return ResponseBean.success();
    }

    @PostMapping("/delete")
    ResponseBean deleteTestCase(@RequestBody AutomatedTestCaseConfigurationDTO automatedTestCaseConfigurationDTO){
        AutoTestCaseConfiguration autoTestCaseConfiguration = AutomatedTestCaseConfigurationDTO.convert(automatedTestCaseConfigurationDTO);
        autoTestCaseConfigurationService.deleteAutoTestCaseConfiguration(autoTestCaseConfiguration);
        return ResponseBean.success();
    }

    @PostMapping("/update")
    ResponseBean updateTestCase(@RequestBody AutomatedTestCaseConfigurationDTO automatedTestCaseConfigurationDTO){
        AutoTestCaseConfiguration autoTestCaseConfiguration = AutomatedTestCaseConfigurationDTO.convert(automatedTestCaseConfigurationDTO);
        autoTestCaseConfigurationService.updateAutoTestCaseConfiguration(autoTestCaseConfiguration);
        return ResponseBean.success();
    }

    @GetMapping("/businessLine/{businessLine}")
    ResponseBean getAutoCaseConfigurationByBusinessLine(@PathVariable String businessLine){
        ArrayList<AutoTestCaseConfiguration> queryResult = autoTestCaseConfigurationService.getAutoTestCaseConfigurationByBusinessLine(businessLine);
        return ResponseBean.success(AutoTestCaseConfiguration.convertToResult(queryResult));
    }

    @GetMapping("/all/businessLine")
    ResponseBean getAllBusinessLine(){
        ArrayList<String> queryResult = autoTestCaseConfigurationService.getAllBusinessLine();
        return ResponseBean.success(queryResult)
    }

    @PostMapping("/businessLine/update/name")
    ResponseBean updateBusinessLine(@RequestBody HashMap<String,String> body){
        if ("newName" in body.keySet() && "oldName" in body.keySet() && body["newName"].length() > 0) {
            int Num = autoTestCaseConfigurationService.updateBusinessLineName(body["newName"], body["oldName"]);
            return ResponseBean.success(Num.toString())
        }
        return ResponseBean.error()
    }

    @PostMapping("/execute/{businessLine}")
    ResponseBean execute(@PathVariable String businessLine){
        try {
            executorService.execute({
                autoTestCaseConfigurationService.executeAutomatedTask(businessLine)
            })
            return ResponseBean.success()
        } catch (Exception e){
            e.printStackTrace()
            return ResponseBean.error()
        }
    }
}

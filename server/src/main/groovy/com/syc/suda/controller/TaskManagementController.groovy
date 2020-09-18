package com.syc.suda.controller

import com.syc.suda.vo.ResponseBean
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.context.ApplicationContext

import java.lang.reflect.Method

@Slf4j
@RestController
@RequestMapping("/task")
class TaskManagementController {

    @Autowired
    ApplicationContext applicationContext


    @RequestMapping("/{taskBeanName}/{taskMethodName}/run")
    ResponseBean run(@PathVariable(name="taskBeanName") String taskBeanName, @PathVariable(name="taskMethodName")String taskMethodName){
        taskBeanName = taskBeanName.uncapitalize()
        Object bean = null
        try {
            bean = applicationContext.getBean(taskBeanName)
            if (!bean){
                String msg = "Bean ${taskBeanName} doesn't exist"
                return ResponseBean.error(msg);
            }
        } catch(Exception e){
            e.printStackTrace()
            return ResponseBean.error(e.printStackTrace())
        }
        try {
            Method method = bean.class.getMethod(taskMethodName)
            Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class)
            if (!scheduledAnnotation){
                String msg = "Bean ${taskBeanName} doesn't have the method ${taskMethodName}"
                log.info(msg)
                return ResponseBean.error(msg)
            }
            method.invoke(bean)
            return ResponseBean.success()
        } catch (Exception e) {
            e.printStackTrace()
            return ResponseBean.error(e.printStackTrace())
        }
    }
}

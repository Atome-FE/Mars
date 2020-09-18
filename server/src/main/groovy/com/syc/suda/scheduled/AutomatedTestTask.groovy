package com.syc.suda.scheduled

import com.syc.suda.service.AutoTestCaseConfigurationService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Slf4j
@Component
class AutomatedTestTask {
    final ExecutorService executorService = new ThreadPoolExecutor(3, 5, 240, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20));

    @Autowired
    AutoTestCaseConfigurationService autoTestCaseConfigurationService

    @Scheduled(cron = '0 0 18 * * *')
    void run(){
        ArrayList<String> allBusinessLineList = autoTestCaseConfigurationService.getAllBusinessLine();
        allBusinessLineList.forEach({ businessLine ->
            executorService.execute({
                autoTestCaseConfigurationService.executeAutomatedTask(businessLine)
            })
        });
    }
}













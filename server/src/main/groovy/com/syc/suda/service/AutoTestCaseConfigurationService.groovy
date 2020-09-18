package com.syc.suda.service

import com.syc.suda.entity.AutoTestCaseConfiguration
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@CompileStatic
interface AutoTestCaseConfigurationService {
    int addNewAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration);

    int updateAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration);

    int deleteAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration);

    ArrayList<AutoTestCaseConfiguration> getAutoTestCaseConfigurationByBusinessLine(String businessLine);

    ArrayList<String> getAllBusinessLine();

    int updateBusinessLineName(String newName, String oldName);

    void executeAutomatedTask(String businessLine);
}
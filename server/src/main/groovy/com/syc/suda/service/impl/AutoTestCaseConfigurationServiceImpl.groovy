package com.syc.suda.service.impl

import com.syc.suda.entity.AutoTestCaseConfiguration
import com.syc.suda.mapper.AutoTestCaseConfigurationMapper
import com.syc.suda.service.AutoTestCaseConfigurationService
import com.syc.suda.service.VariableService
import com.syc.suda.util.ShellUtil
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import java.text.SimpleDateFormat

@Slf4j
@Service
@CompileStatic
class AutoTestCaseConfigurationServiceImpl implements AutoTestCaseConfigurationService{

    @Autowired
    AutoTestCaseConfigurationMapper autoTestCaseConfigurationMapper;

    @Autowired
    AutoTestCaseConfigurationService autoTestCaseConfigurationService

    @Autowired
    VariableService variableService

    @Override
    int addNewAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration) {
        return autoTestCaseConfigurationMapper.add(autoTestCaseConfiguration);
    }

    @Override
    int updateAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration) {
        return autoTestCaseConfigurationMapper.update(autoTestCaseConfiguration);
    }

    @Override
    int deleteAutoTestCaseConfiguration(AutoTestCaseConfiguration autoTestCaseConfiguration) {
        return autoTestCaseConfigurationMapper.delete(autoTestCaseConfiguration.getId());
    }

    @Override
    ArrayList<AutoTestCaseConfiguration> getAutoTestCaseConfigurationByBusinessLine(String businessLine) {
        return autoTestCaseConfigurationMapper.getAutoTestCaseConfigurationByBusinessLine(businessLine);
    }

    @Override
    ArrayList<String> getAllBusinessLine() {
        return autoTestCaseConfigurationMapper.getAllBusinessLine()
    }

    @Override
    int updateBusinessLineName(String newName, String oldName) {
        return autoTestCaseConfigurationMapper.updateBusinessLineName(newName, oldName);
    }

    @Override
    void executeAutomatedTask(String businessLine) {
        log.info("start execute automated task for " + businessLine)
        ArrayList<AutoTestCaseConfiguration> autoTestCaseConfigurationList = autoTestCaseConfigurationService.getAutoTestCaseConfigurationByBusinessLine(businessLine);
        Date date = new Date();
        File businessLineDir = new File(businessLine);
        businessLineDir.mkdir();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String dir = businessLine + "/" + businessLine + ft.format(date) + "-" + System.currentTimeMillis().toString() + ".sh"
        String content = ""
        content += "ssh " + variableService.getValueByName("user") + "@" + variableService.getValueByName("ip") + " /bin/bash << 'EOF1';\n"
        content += autoTestCaseConfigurationList[0].getSshVisit() + " /bin/bash << 'EOF'\n"
        autoTestCaseConfigurationList.forEach({
            config ->
                content += "cd " + config.getAbsoluteDir() + ";\n";
                content += config.mainCommend + " " + config.getRelativeDir() + " " + config.getParameter() + ";\n";
                content += "sleep " + (variableService.getValueByName("autoTestSleep")?:"60s") + ";\n";

        })
        content += "exit;\n"
        content += "EOF;\n"
        content += "exit;\n"
        content += "EOF1;\n"
        ShellUtil.sshClientToExec(dir,content)
        log.info("finish execute automated task for " + businessLine)
    }
}

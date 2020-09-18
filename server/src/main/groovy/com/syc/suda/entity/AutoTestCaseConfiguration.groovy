package com.syc.suda.entity

import com.syc.suda.dto.AutomatedTestCaseConfigurationDTO
import groovy.util.logging.Slf4j

@Slf4j
class AutoTestCaseConfiguration {
    private int id;
    private String businessLine;
    private String sshVisit;
    private String absoluteDir;
    private String mainCommend;
    private String relativeDir;
    private String parameter;

    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    String getBusinessLine() {
        return businessLine
    }

    void setBusinessLine(String businessLine) {
        this.businessLine = businessLine
    }

    String getSshVisit() {
        return sshVisit
    }

    void setSshVisit(String sshVisit) {
        this.sshVisit = sshVisit
    }

    String getAbsoluteDir() {
        return absoluteDir
    }

    void setAbsoluteDir(String absoluteDir) {
        this.absoluteDir = absoluteDir
    }

    String getMainCommend() {
        return mainCommend
    }

    void setMainCommend(String mainCommend) {
        this.mainCommend = mainCommend
    }

    String getRelativeDir() {
        return relativeDir
    }

    void setRelativeDir(String relativeDir) {
        this.relativeDir = relativeDir
    }

    String getParameter() {
        return parameter
    }

    void setParameter(String parameter) {
        this.parameter = parameter
    }

    public static ArrayList<AutomatedTestCaseConfigurationDTO> convertToResult(ArrayList<AutoTestCaseConfiguration> autoTestCaseConfigurationList){
        ArrayList<AutomatedTestCaseConfigurationDTO> result = new ArrayList<>();

        autoTestCaseConfigurationList.forEach({ autoTestCaseConfiguration ->
            AutomatedTestCaseConfigurationDTO automatedTestCaseConfigurationDTO = new AutomatedTestCaseConfigurationDTO();
            automatedTestCaseConfigurationDTO.setId(autoTestCaseConfiguration.getId().toString());
            automatedTestCaseConfigurationDTO.setSshVisit(autoTestCaseConfiguration.getSshVisit());
            automatedTestCaseConfigurationDTO.setMainCommend(autoTestCaseConfiguration.getMainCommend());
            automatedTestCaseConfigurationDTO.setAbsoluteDir(autoTestCaseConfiguration.getAbsoluteDir());
            automatedTestCaseConfigurationDTO.setRelativeDir(autoTestCaseConfiguration.getRelativeDir());
            automatedTestCaseConfigurationDTO.setParameter(autoTestCaseConfiguration.getParameter());
            automatedTestCaseConfigurationDTO.setBusinessLine(autoTestCaseConfiguration.getBusinessLine());
            result.add(automatedTestCaseConfigurationDTO)
        })
        log.info(result.toString());
        return result;
    }
}

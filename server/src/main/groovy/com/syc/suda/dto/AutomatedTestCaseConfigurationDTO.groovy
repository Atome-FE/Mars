package com.syc.suda.dto

import com.syc.suda.entity.AutoTestCaseConfiguration

class AutomatedTestCaseConfigurationDTO {
    private String id;
    private String businessLine;
    private String sshVisit;
    private String absoluteDir;
    private String mainCommend;
    private String relativeDir;
    private String parameter;

    String getId() {
        return id
    }

    void setId(String id) {
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

    public static AutoTestCaseConfiguration convert(AutomatedTestCaseConfigurationDTO automatedTestCaseConfigurationDTO){
        AutoTestCaseConfiguration autoTestCaseConfiguration = new AutoTestCaseConfiguration();
        autoTestCaseConfiguration.setBusinessLine(automatedTestCaseConfigurationDTO.getBusinessLine());
        autoTestCaseConfiguration.setAbsoluteDir(automatedTestCaseConfigurationDTO.getAbsoluteDir());
        autoTestCaseConfiguration.setMainCommend(automatedTestCaseConfigurationDTO.getMainCommend());
        autoTestCaseConfiguration.setRelativeDir(automatedTestCaseConfigurationDTO.getRelativeDir());
        autoTestCaseConfiguration.setParameter(automatedTestCaseConfigurationDTO.getParameter());
        autoTestCaseConfiguration.setSshVisit(automatedTestCaseConfigurationDTO.getSshVisit());
        if (automatedTestCaseConfigurationDTO.getId()){
            autoTestCaseConfiguration.setId(Integer.valueOf(automatedTestCaseConfigurationDTO.getId()));
        }
        return autoTestCaseConfiguration;
    }

}

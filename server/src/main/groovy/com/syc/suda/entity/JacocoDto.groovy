package com.syc.suda.entity

class JacocoDto {
    int id
    String ip
    String ports
    String projectDirectory
    String reportDirectory
    String identity
    String knownHost
    String branchName
    String newTag
    String oldTag
    List<String> excludeEnd
    String remoteUri
    int needCompile
    int deleted

    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    int getDeleted() {
        return deleted
    }

    void setDeleted(int deleted) {
        this.deleted = deleted
    }

    int getNeedCompile() {
        return needCompile
    }

    void setNeedCompile(int needCompile) {
        this.needCompile = needCompile
    }

    String getRemoteUri() {
        return remoteUri
    }

    void setRemoteUri(String remoteUri) {
        this.remoteUri = remoteUri
    }

    List<String> getExcludeEnd() {
        return excludeEnd
    }

    void setExcludeEnd(List<String> excludeEnd) {
        this.excludeEnd = excludeEnd
    }

    String getIp() {
        return ip
    }

    void setIp(String ip) {
        this.ip = ip
    }

    String getPorts() {
        return ports
    }

    void setPorts(String ports) {
        this.ports = ports
    }

    String getProjectDirectory() {
        return projectDirectory
    }

    void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory
    }

    String getReportDirectory() {
        return reportDirectory
    }

    void setReportDirectory(String reportDirectory) {
        this.reportDirectory = reportDirectory
    }

    String getIdentity() {
        return identity
    }

    void setIdentity(String identity) {
        this.identity = identity
    }

    String getKnownHost() {
        return knownHost
    }

    void setKnownHost(String knownHost) {
        this.knownHost = knownHost
    }

    String getBranchName() {
        return branchName
    }

    void setBranchName(String branchName) {
        this.branchName = branchName
    }

    String getNewTag() {
        return newTag
    }

    void setNewTag(String newTag) {
        this.newTag = newTag
    }

    String getOldTag() {
        return oldTag
    }

    void setOldTag(String oldTag) {
        this.oldTag = oldTag
    }
}

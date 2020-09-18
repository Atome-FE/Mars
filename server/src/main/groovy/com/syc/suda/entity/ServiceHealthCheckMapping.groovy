package com.syc.suda.entity

class ServiceHealthCheckMapping {
    String id
    String userId
    String dockerName
    String cmd
    String restartCmd
    String stopCmd
    Boolean needCheck
    String environmentName
    String status
    Boolean heathly
}

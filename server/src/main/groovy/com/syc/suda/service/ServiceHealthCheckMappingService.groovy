package com.syc.suda.service

import com.syc.suda.entity.ServiceHealthCheckMapping

interface ServiceHealthCheckMappingService {
    int save(ServiceHealthCheckMapping serviceHealthCheckMapping)

    int delete(String id)

    List<ServiceHealthCheckMapping> listByUserId(String userId)

    List<ServiceHealthCheckMapping> listAll()

    int update(ServiceHealthCheckMapping serviceHealthCheckMapping)

}
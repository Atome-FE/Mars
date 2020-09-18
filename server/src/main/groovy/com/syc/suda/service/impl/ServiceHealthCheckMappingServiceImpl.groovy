package com.syc.suda.service.impl

import com.syc.suda.entity.ServiceHealthCheckMapping
import com.syc.suda.mapper.ServiceHealthCheckMappingMapper
import com.syc.suda.service.ServiceHealthCheckMappingService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServiceHealthCheckMappingServiceImpl implements ServiceHealthCheckMappingService {

    static final String PREFIX = 'S'

    @Autowired
    ServiceHealthCheckMappingMapper mapper

    @Override
    int save(ServiceHealthCheckMapping serviceHealthCheckMapping) {
        serviceHealthCheckMapping.id = IdGenerator.generate(PREFIX)
        return mapper.save(serviceHealthCheckMapping)
    }

    @Override
    int delete(String id) {
        return mapper.delete(id)
    }

    @Override
    List<ServiceHealthCheckMapping> listByUserId(String userId) {
        return mapper.listByUserId(userId).collect {
            it.heathly = false
            return it
        }
    }

    @Override
    List<ServiceHealthCheckMapping> listAll() {
        return mapper.listAll()
    }

    @Override
    int update(ServiceHealthCheckMapping serviceHealthCheckMapping) {
        return mapper.update(serviceHealthCheckMapping)
    }
}

package com.syc.suda.service.impl

import com.syc.suda.entity.AppMapping
import com.syc.suda.mapper.AppMappingMapper
import com.syc.suda.service.AppMappingService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class AppMappingServiceImpl implements AppMappingService {

    @Autowired
    AppMappingMapper appMappingMapper

    @Override
    List<AppMapping> listAppMapping() {
        return appMappingMapper.listAppMapping()
    }
}

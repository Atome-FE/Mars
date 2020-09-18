package com.syc.suda.service.impl

import com.syc.suda.mapper.VariableMapper
import com.syc.suda.service.VariableService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VariableServiceImpl implements VariableService{

    @Autowired
    VariableMapper variableMapper

    @Override
    String getValueByName(String name) {
        return variableMapper.getValueByName(name);
    }

    @Override
    int updateValue(String name, String value) {
        return variableMapper.setValue(name, value);
    }
}

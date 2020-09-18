package com.syc.suda.service.impl

import com.syc.suda.entity.TestGroupCase
import com.syc.suda.mapper.TestGroupCaseBaseMapper
import com.syc.suda.service.TestGroupCaseService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class TestGroupCaseServiceImpl implements TestGroupCaseService {

    private static final String ID_PREFIX = 'C'

    @Autowired
    TestGroupCaseBaseMapper testGroupCaseBaseMapper

    @Override
    int save(TestGroupCase testGroupCase) {
        testGroupCase.id = IdGenerator.generate(ID_PREFIX)
        return testGroupCaseBaseMapper.save(testGroupCase)
    }

    @Override
    List<TestGroupCase> listTestGroupCase(String userId) {
        return testGroupCaseBaseMapper.listTestGroupCase(userId)
    }

    @Override
    int deleteById(String id) {
        return testGroupCaseBaseMapper.deleteById(id)
    }

    @Override
    int updatePriorityById(String id, int priority) {
        return testGroupCaseBaseMapper.updatePriorityById(id, priority)
    }
}

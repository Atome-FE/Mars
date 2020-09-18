package com.syc.suda.service.impl

import com.syc.suda.entity.TestGroup
import com.syc.suda.mapper.TestGroupBaseMapper
import com.syc.suda.service.TestGroupService
import com.syc.suda.util.IdGenerator
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class TestGroupServiceImpl implements TestGroupService {

    private static final String ID_PREFIX = 'G'

    @Autowired
    TestGroupBaseMapper testGroupBaseMapper

    @Override
    int save(TestGroup testGroup) {
        testGroup.id = IdGenerator.generate(ID_PREFIX)
        return testGroupBaseMapper.save(testGroup)
    }

    @Override
    List<TestGroup> listTestGroup(String userId) {
        return testGroupBaseMapper.listTestGroup(userId)
    }

    @Override
    int update(TestGroup testGroup) {
        return testGroupBaseMapper.update(testGroup)
    }

    @Override
    int deleteById(String id) {
        return testGroupBaseMapper.delete(id)
    }
}

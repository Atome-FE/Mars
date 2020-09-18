package com.syc.suda.service.impl

import com.syc.suda.entity.TestCase
import com.syc.suda.enums.MaterialType
import com.syc.suda.mapper.TestCaseBaseMapper
import com.syc.suda.service.TestCaseService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestCaseServiceImpl implements TestCaseService {

    private static final String ID_PREFIX = 'T'

    @Autowired
    TestCaseBaseMapper testCaseBaseMapper

    @Override
    int save(TestCase testCase) {
        testCase.id = IdGenerator.generate(ID_PREFIX)
        return testCaseBaseMapper.save(testCase)
    }

    @Override
    int update(TestCase testCase) {
        return testCaseBaseMapper.update(testCase)
    }

    @Override
    TestCase getById(String id) {
        return testCaseBaseMapper.getById(id)
    }

    @Override
    List<TestCase> listByIds(List<String> ids) {
        return testCaseBaseMapper.listByIds(ids)
    }

    @Override
    List<TestCase> listTestCase(String userId) {
        List<TestCase> list = testCaseBaseMapper.listTestCase(userId)
        list.each { it.materialType = MaterialType.CASE }
        return list
    }

    @Override
    int deleteById(String id) {
        return testCaseBaseMapper.deleteById(id)
    }
}

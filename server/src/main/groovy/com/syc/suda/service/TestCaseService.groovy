package com.syc.suda.service

import com.syc.suda.entity.TestCase

interface TestCaseService {

    int save(TestCase testCase)

    int update(TestCase testCase)

    TestCase getById(String id)

    List<TestCase> listByIds(List<String> ids)

    List<TestCase> listTestCase(String userId)

    int deleteById(String id)

}
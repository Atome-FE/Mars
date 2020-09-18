package com.syc.suda.service

import com.syc.suda.entity.TestGroupCase

interface TestGroupCaseService {

    int save(TestGroupCase testGroupCase)

    List<TestGroupCase> listTestGroupCase(String userId)

    int deleteById(String id)

    int updatePriorityById(String id, int priority)

}
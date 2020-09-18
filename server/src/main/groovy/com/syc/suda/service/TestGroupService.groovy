package com.syc.suda.service

import com.syc.suda.entity.TestGroup
import groovy.transform.CompileStatic

@CompileStatic
interface TestGroupService {

    int save(TestGroup testGroup)

    List<TestGroup> listTestGroup(String userId)

    int update(TestGroup testGroup)

    int deleteById(String id)

}
package com.syc.suda.entity

import javax.validation.constraints.NotBlank

class TestGroupCase {

    String id

    String userId

    String environment

    @NotBlank
    String testGroupId

    @NotBlank
    String testCaseId

    int delay

    String assertCondition

    String preCondition

    String postCondition

    int priority
}

package com.syc.suda.entity

import javax.validation.constraints.NotBlank

class TestGroup {

    String id

    String userId

    @NotBlank
    String name

    String environment

    String material

    Boolean concurrency
}

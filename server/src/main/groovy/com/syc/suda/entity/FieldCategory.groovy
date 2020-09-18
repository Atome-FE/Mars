package com.syc.suda.entity

import javax.validation.constraints.AssertTrue
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class FieldCategory {

    String id

    String dataType

    String category

    @Pattern(regexp = "chen|lin", message = "手机号码错误")
    String name
}

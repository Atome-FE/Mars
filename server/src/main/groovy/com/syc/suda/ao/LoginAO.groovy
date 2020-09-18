package com.syc.suda.ao

import groovy.transform.CompileStatic

import javax.validation.constraints.NotNull

@CompileStatic
class LoginAO {
    @NotNull
    String email

    @NotNull
    String password
}

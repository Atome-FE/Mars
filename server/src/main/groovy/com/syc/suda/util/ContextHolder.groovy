package com.syc.suda.util

import com.syc.suda.model.CommonUserDetails
import groovy.transform.CompileStatic
import org.springframework.security.core.context.SecurityContextHolder

@CompileStatic
class ContextHolder {
    static String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName()
    }

    static CommonUserDetails getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() as CommonUserDetails
    }
}

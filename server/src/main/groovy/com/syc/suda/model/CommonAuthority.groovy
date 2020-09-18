package com.syc.suda.model

import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority

@CompileStatic
class CommonAuthority implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 2170464529950854333L

    Long id
    String name
    String authority
    String description
    String method

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        CommonAuthority that = (CommonAuthority) o

        if (authority != that.authority || method != that.method) return false

        return true
    }

    int hashCode() {
        return (authority != null ? authority.hashCode() + method.hashCode() : 0)
    }
}

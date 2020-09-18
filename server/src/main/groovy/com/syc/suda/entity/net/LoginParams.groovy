package com.syc.suda.entity.net

import javax.validation.constraints.NotNull

class LoginParams {
    @NotNull
    String mobileNumber

    @NotNull
    String password
}

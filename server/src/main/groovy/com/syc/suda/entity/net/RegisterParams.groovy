package com.syc.suda.entity.net

import javax.validation.constraints.NotNull

class RegisterParams {
    @NotNull
    String mobileNumber

    @NotNull
    String password
}

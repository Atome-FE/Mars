package com.syc.suda.service

import com.syc.suda.ao.LoginAO
import com.syc.suda.entity.net.RegisterParams

interface AuthService {

    void logout()

    String login(LoginAO loginAO)

    /*
    String loginV2(LoginAO loginAO)
    */

    String safeRegister(RegisterParams registerParams)

}
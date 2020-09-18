package com.syc.suda.service

import com.syc.suda.dto.UserDTO

interface UserService {

    int save(UserDTO userDTO)

    UserDTO getUserByMobile(String mobileNumber)

    UserDTO verifyPasswordAndGet(String mobileNumber, String password)

}
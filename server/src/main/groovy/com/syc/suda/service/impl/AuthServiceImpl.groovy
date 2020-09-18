package com.syc.suda.service.impl

import com.syc.suda.ao.LoginAO
import com.syc.suda.dto.UserDTO
import com.syc.suda.entity.net.RegisterParams
import com.syc.suda.exception.AccountExistException
import com.syc.suda.exception.UserNotFoundException
import com.syc.suda.model.CommonAdmin
import com.syc.suda.service.AuthService
import com.syc.suda.service.CommonSecurityService
import com.syc.suda.service.UserService
import com.syc.suda.util.IdGenerator
import com.syc.suda.util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl implements AuthService {

    @Autowired
    JwtUtils jwtUtils

    @Autowired
    CommonSecurityService commonSecurityService

    @Autowired
    UserService userService

    void checkMobileExists(String mobileNumber) {
        UserDTO userDTO = userService.getUserByMobile(mobileNumber)
        if (userDTO) {
            if (userDTO.password) {
                throw new AccountExistException('用户已经存在')
            }
        }
    }

    @Override
    void logout() {
        jwtUtils.removeAccessToken()
    }

    /*
    @Override
    String login(String mobileNumber, String password) {
        UserDTO userDTO = userService.verifyPasswordAndGet(mobileNumber, password)
        String token = jwtUtils.sign(userDTO.generateSessionInfo())
        jwtUtils.saveAccessToken(token)
        return userDTO.id
    }
    */

    @Override
    String login(LoginAO loginAO) {
        CommonAdmin commonAdmin = commonSecurityService.getByEmailAndPassword(loginAO.email, loginAO.password)
        UserDTO userDTO = UserDTO.toDTO(commonAdmin)
        if (!userDTO) {
            throw new UserNotFoundException('用户不存在')
        }
        String token = jwtUtils.sign(userDTO.generateSessionInfo())
        jwtUtils.saveAccessToken(token)
        return userDTO.id
    }

    @Override
    String safeRegister(RegisterParams registerParams) {
        checkMobileExists(registerParams.mobileNumber)
        return register(registerParams.mobileNumber, registerParams.password)
    }

    String register(String mobileNumber, String password) {
        UserDTO userDTO = new UserDTO(mobileNumber: mobileNumber, password: password)
        doRegister(userDTO)
        return userDTO.id
    }

    private void doRegister(UserDTO userDTO) {
        userDTO.id = IdGenerator.generate(UserDTO.ID_PREFIX)
        userService.save(userDTO)
        String token = jwtUtils.sign(userDTO.generateSessionInfo())
        jwtUtils.saveAccessToken(token)
    }
}

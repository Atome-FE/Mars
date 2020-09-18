package com.syc.suda.service.impl

import com.syc.suda.dto.UserDTO
import com.syc.suda.entity.User
import com.syc.suda.exception.UserNotFoundException
import com.syc.suda.mapper.UserBaseMapper
import com.syc.suda.security.EncryptStringField
import com.syc.suda.security.HashStringField
import com.syc.suda.service.UserService
import org.springframework.beans.factory.annotation.Autowired
// TODO 重要字段加密
// 引入 implementation 'org.springframework.boot:spring-boot-starter-security'
// import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    /*
    @Autowired
    PasswordEncoder passwordEncoder
    */

    @Autowired
    UserBaseMapper userBaseMapper

    @Override
    int save(UserDTO userDTO) {
        /*
        if (userDTO.password){
            userDTO.password = encodePassword(userDTO.password)
        }
        */
        User user = toEntity(userDTO)
        int row = userBaseMapper.save(user)
        return row
    }

    User toEntity(UserDTO dto) {
        User user = new User()
        user.id = dto.id
        user.mobileNumber = dto.mobileNumber
        user.mobileNumberEncrypted = dto.mobileNumber ? new EncryptStringField(dto.mobileNumber) : (EncryptStringField) null
        user.mobileNumberHashed = new HashStringField(dto.mobileNumber)
        user.password = dto.password
        user.createTimestamp = dto.createTimestamp
        user.updateTimestamp = dto.updateTimestamp
        return user
    }

    @Override
    UserDTO getUserByMobile(String mobileNumber) {
        // TODO 支持加密查询
        // User userDO = userBaseMapper.getByMobile(new EncryptStringField(mobileNumber))
        User userDO = userBaseMapper.getByMobile(mobileNumber)
        UserDTO user = toDTO(userDO)
        return user
    }

    @Override
    UserDTO verifyPasswordAndGet(String mobileNumber, String password) {
        // TODO 支持加密查询
        // User user = userBaseMapper.getByMobile(new EncryptStringField(mobileNumber))
        User user = userBaseMapper.getByMobile(mobileNumber)
        UserDTO userDTO = toDTO(user)
        if (!userDTO) {
            throw new UserNotFoundException('用户不存在')
        }
        return userDTO
    }

    UserDTO toDTO(User user) {
        if (!user) {
            return null
        } else {
            UserDTO userDTO = new UserDTO()
            userDTO.id = user.id
            userDTO.mobileNumber = user.mobileNumber ?: user.mobileNumberEncrypted?.getCleartext()
            userDTO.password = user.password
            userDTO.createTimestamp = user.createTimestamp
            userDTO.updateTimestamp = user.updateTimestamp
            return userDTO
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password)
    }
}

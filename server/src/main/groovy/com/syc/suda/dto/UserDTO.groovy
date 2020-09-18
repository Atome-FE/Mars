package com.syc.suda.dto

import com.syc.suda.model.CommonAdmin

class UserDTO extends CommonAdmin {
    SessionInfo generateSessionInfo(){
        return new SessionInfo(id: id, email: email, username: username)
    }

    static UserDTO toDTO(CommonAdmin user) {
        if (!user) {
            return null
        } else {
            UserDTO userDTO = new UserDTO()
            userDTO.id = user.id
            userDTO.email = user.email
            userDTO.username = user.username
            return userDTO
        }
    }
}

package com.syc.suda.entity

import com.syc.suda.security.EncryptStringField
import com.syc.suda.security.HashStringField
import groovy.transform.CompileStatic

@CompileStatic
class User {

    String id

    String mobileNumber

    EncryptStringField mobileNumberEncrypted

    HashStringField mobileNumberHashed

    String password

    Date createTimestamp
    Date updateTimestamp

}

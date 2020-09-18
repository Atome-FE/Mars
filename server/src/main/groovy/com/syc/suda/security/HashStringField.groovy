package com.syc.suda.security

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = HashStringFieldDeserializer.class)
class HashStringField {

    /**
     * 原始数据
     */
    private String cleartext;

    HashStringField(String cleartext) {
        this.cleartext = cleartext
    }

    String getCleartext() {
        return cleartext
    }

    @Override
    String toString() {
        return getCleartext()
    }
}

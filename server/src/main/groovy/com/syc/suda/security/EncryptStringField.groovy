package com.syc.suda.security

class EncryptStringField {
    /**
     * 原始数据
     */
    private String cleartext

    EncryptStringField(String cleartext) {
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

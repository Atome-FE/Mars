package com.syc.suda.service

interface VariableService {
    String getValueByName(String name);

    int updateValue(String name, String value);
}
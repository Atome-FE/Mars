package com.syc.suda.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic

@CompileStatic
class JsonUtil {
    static final ObjectMapper COMMON_OBJECT_MAPPER

    static {
        COMMON_OBJECT_MAPPER = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    static <T> T parse(String jsonString, Class<T> clazz) {
        return COMMON_OBJECT_MAPPER.readValue(jsonString, clazz)
    }

    static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        return COMMON_OBJECT_MAPPER.readValue(jsonString, typeReference)
    }

    static String toString(Object object) {
        return COMMON_OBJECT_MAPPER.writeValueAsString(object)
    }
}

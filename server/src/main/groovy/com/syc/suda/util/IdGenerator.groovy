package com.syc.suda.util

import org.bson.types.ObjectId

class IdGenerator {
    static String generate(String prefix) {
        return prefix + ObjectId.get().toString().toUpperCase()
    }
}

package com.syc.suda.entity

import com.syc.suda.enums.MaterialType

class Env extends Material {
    String http

    String sql

    String redis

    String mq

    String globalVariable

    String mongo

    String describe

    MaterialType materialType
}

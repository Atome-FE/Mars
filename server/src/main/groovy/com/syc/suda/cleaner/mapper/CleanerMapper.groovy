package com.syc.suda.cleaner.mapper

import com.syc.suda.entity.DataMaterial
import com.syc.suda.entity.Env
import com.syc.suda.entity.HttpMaterial
import com.syc.suda.entity.MongoMaterial
import com.syc.suda.entity.MqMaterial
import com.syc.suda.entity.RedisMaterial
import com.syc.suda.entity.SqlMaterial
import com.syc.suda.entity.TestCase
import org.apache.ibatis.annotations.Select

interface CleanerMapper {
    @Select('''
    select * from http_material
    ''')
    List<HttpMaterial> allHttpMaterial()
    @Select('''
    select * from mongo_material
    ''')
    List<MongoMaterial> allMongoMaterial()
    @Select('''
    select * from mq_material
    ''')
    List<MqMaterial> allMqMaterial()
    @Select('''
    select * from redis_material
    ''')
    List<RedisMaterial> allRedisMaterial()
    @Select('''
    select * from sql_material
    ''')
    List<SqlMaterial> allSqlMaterial()
    @Select('''
    select * from data_material
    ''')
    List<DataMaterial> allDataMaterial()
    @Select('''
    select * from env_config
    ''')
    List<Env> allEnvMaterial()
    @Select('''
    select * from test_case
    ''')
    List<TestCase> allCaseMaterial()

}
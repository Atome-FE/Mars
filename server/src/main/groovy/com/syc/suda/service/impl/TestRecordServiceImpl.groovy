package com.syc.suda.service.impl

import com.syc.suda.entity.TestRecord
import com.syc.suda.mapper.TestRecordMapper
import com.syc.suda.service.TestRecordService
import com.syc.suda.util.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestRecordServiceImpl implements TestRecordService {

    private static final String ID_PREFIX = 'R'

    @Autowired
    TestRecordMapper testRecordMapper

    @Override
    int save(TestRecord testRecord) {
        testRecord.id = IdGenerator.generate(ID_PREFIX)
        return testRecordMapper.save(testRecord)
    }
}

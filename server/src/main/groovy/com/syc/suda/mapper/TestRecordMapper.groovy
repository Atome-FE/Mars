package com.syc.suda.mapper

import com.syc.suda.entity.TestRecord
import org.apache.ibatis.annotations.Insert

interface TestRecordMapper {

    @Insert('''
    insert into test_record(id,
    test_group_case_id,
    record)
    values(#{id},
    #{testGroupCaseId},
    #{record})
    ''')
    int save(TestRecord testRecord)

    TestRecord getByTestGroupCaseId(String testGroupCaseId)

    List<TestRecord> listByTestGroupCaseIds(String testGroupCaseIds)
}
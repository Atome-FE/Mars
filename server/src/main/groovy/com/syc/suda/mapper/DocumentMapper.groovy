package com.syc.suda.mapper

import com.syc.suda.entity.Document
import groovy.transform.CompileStatic
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@CompileStatic
interface DocumentMapper {

    @Insert('''
    insert into document(
    id,
    ext_id,
    `name`,
    user_id,
    content,
    md_content
    )
    values(
    #{id},
    #{extId},
    #{name},
    #{userId},
    #{content},
    #{mdContent}
    )
    ''')
    int save(Document document)

    @Update('''
    update document
    set `name` = #{name},
    ext_id = #{extId},
    content = #{content},
    md_content = #{mdContent}
    where
    id = #{id}
    ''')
    int update(Document document)

    @Select('''
    select id,
    `name`,
    content,
    ext_id as extId,
    md_content as mdContent
    from document where deleted = 0
    ''')
    List<Document> listDocument()

    @Select('''
    select id,
    `name`,
    content,
    ext_id as extId,
    md_content as mdContent,
    `lock`
    from document
    where
    id = #{id} and deleted = 0
    ''')
    Document getById(@Param('id') String id)

    @Update('''
    update document
    set `lock` = #{lock}
    where id = #{id}
    ''')
    int updateDocumentLock(@Param('lock') int lock, @Param('id') String id)

    @Update('''
    update document set deleted = 1
    where id = #{id}
    ''')
    int deleteById(@Param('id') String id)

}
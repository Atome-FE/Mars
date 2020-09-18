package com.syc.suda.controller

import com.syc.suda.enums.ResponseCodeEnum
import com.syc.suda.vo.ResponseBean
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Slf4j
@CompileStatic
@RestController
@RequestMapping('/file')
class FileController {

    @Value('${t.file.path}')
    private String filePath

    @PostMapping('/upload')
    ResponseBean upload(@RequestParam('file') MultipartFile file) {
        String fileName = file.getOriginalFilename()
        String dest = this.filePath + fileName
        File newFile = new File(dest)
        try {
            log.info('文件保存路径: {}', dest)
            file.transferTo(newFile)
            return ResponseBean.success(dest)
        } catch (Exception e) {
            log.error('文件上传错误')
            return ResponseBean.fail(ResponseCodeEnum.ERROR)
        }
    }
}

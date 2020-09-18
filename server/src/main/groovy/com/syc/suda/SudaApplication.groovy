package com.syc.suda

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@MapperScan("com.syc.suda.**.mapper")
class SudaApplication {

    static void main(String[] args) {
        SpringApplication.run(SudaApplication, args)
    }

}

package com.xuecheng.content;

import com.xuecheng.base.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author Zihao Qin
 * @Date 2023/12/4 21:08
 */
// 导入其他模块的类
@Import(GlobalExceptionHandler.class)
@SpringBootApplication
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}

package com.yida.spider4j.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

@SpringBootApplication(scanBasePackages = {"com.yida.spider4j.crawler"})
@EnableWebMvc
@EnableScheduling
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        log.info("Spring Boot is starting...");
        setDefaultCharset();

        SpringApplication.run(Application.class, args);
        log.info("Spring Boot had started successfully.");
    }

    private static void setDefaultCharset() {
        Field[] fields = Charset.class.getDeclaredFields();
        for(Field field : fields) {
            if(!"defaultCharset".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(null, Charset.forName("UTF-8"));
            } catch (Exception e) {
                log.error("set system default charset occur exception:{}", e.getMessage());
            }
        }
    }
}

package org.example.dlf_web_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DlfWebBackendApplication {

    static void main(String[] args) {
        SpringApplication.run(DlfWebBackendApplication.class, args);
    }
}

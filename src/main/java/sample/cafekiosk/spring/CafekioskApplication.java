package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  //Auditing 활성화
@SpringBootApplication
public class CafekioskApplication {

    public static void main(String[] args) {

        SpringApplication.run(CafekioskApplication.class, args);
    }

}

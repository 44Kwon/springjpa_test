package sample.cafekiosk.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  //Auditing 활성화
@Configuration
public class JpaAuditingConfig {
}

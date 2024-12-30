package io.hhplus_architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// JPA Auditing 활성화 ( @CreatedDate 사용)
@EnableJpaAuditing
public class HhplusArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhplusArchitectureApplication.class, args);
	}

}

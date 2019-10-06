package lk.gov.moe.gisrestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("lk.gov.moe.gisrestservice.repository")
@EntityScan("lk.gov.moe.gisrestservice.model")
public class GisRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GisRestServiceApplication.class, args);
	}

}

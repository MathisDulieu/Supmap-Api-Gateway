package com.novus.api_gateway;

import com.novus.api_gateway.configuration.DateConfiguration;
import com.novus.api_gateway.configuration.EnvConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.novus.api_gateway")
@EnableConfigurationProperties(EnvConfiguration.class)
public class ApiGatewayApplication {

	private final DateConfiguration dateConfiguration = new DateConfiguration();
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@PostConstruct
	void setLocalTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
		log.info("API Gateway running in Paris timezone, started at: {}", dateConfiguration.newDate());
	}

	@Configuration
	@Profile("test")
	@ComponentScan(lazyInit = true)
	static class ConfigForShorterBootTimeForTests {
	}

}

package com.ronin.sportbook;

import com.ronin.sportbook.common.security.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SportbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportbookApplication.class, args);
	}

}

package com.wipro;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.wipro.properties.FlightProperties;

@SpringBootApplication
//@EnableConfigurationProperties(FlightProperties.class)
public class BrownfieldAirlinesApplication {

	public static void main(String[] args) {
		// SpringApplication.run(BrownfieldAirlinesApplication.class, args);

		ConfigurableApplicationContext app = new SpringApplicationBuilder(BrownfieldAirlinesApplication.class)
				.web(WebApplicationType.SERVLET)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

}

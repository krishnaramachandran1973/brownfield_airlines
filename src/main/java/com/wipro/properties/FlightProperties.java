package com.wipro.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
@ConfigurationPropertiesScan
public class FlightProperties {
	private Map<String, String> common;

	private FlightService flightService = new FlightService();

	@Getter
	@Setter
	@NoArgsConstructor
	public static class FlightService {
		private Map<String, String> getFlights;
		private Map<String, String> findById;
	}

}

package com.wipro;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wipro.dto.FlightDto;

// integration - end to end
// slice 
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BrownfieldAirlinesApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void testGetAll() {
		ParameterizedTypeReference<List<FlightDto>> respType = new ParameterizedTypeReference<List<FlightDto>>() {
		};

		ResponseEntity<List<FlightDto>> respEntity = restTemplate.exchange("/api/flights", HttpMethod.GET, null,
				respType);
		assertThat(respEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(respEntity.getBody()).size()
				.isEqualTo(8);
	}
}

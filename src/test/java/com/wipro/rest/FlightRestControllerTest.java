package com.wipro.rest;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.controllers.FlightRestController;
import com.wipro.dto.FareDto;
import com.wipro.dto.FlightDto;
import com.wipro.exceptions.FlightNotFoundException;
import com.wipro.model.FlightType;
import com.wipro.service.FlightService;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(controllers = FlightRestController.class)
public class FlightRestControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private FlightService flightService;

	@Autowired
	private ObjectMapper objectMapper;

	FlightDto newFlight;

	@BeforeEach
	void setup() {
		BDDMockito.given(flightService.getAllFlights())
				.willReturn(Arrays.asList(FlightDto.builder()
						.id(1L)
						.flightNumber("ABC123")
						.departureAirport("JFK")
						.destinationAirport("LAX")
						.departureTime(LocalDateTime.of(2024, 3, 10, 10, 0))
						.arrivalTime(LocalDateTime.of(2024, 3, 10, 13, 0))
						.availableSeats(150)
						.fareDto(FareDto.builder()
								.amount(200.00)
								.currency("USD")
								.build())
						.type(FlightType.DOMESTIC)
						.build(),
						FlightDto.builder()
								.id(2L)
								.flightNumber("DEF456")
								.departureAirport("LAX")
								.destinationAirport("JFK")
								.departureTime(LocalDateTime.of(2024, 3, 11, 11, 0))
								.arrivalTime(LocalDateTime.of(2024, 3, 11, 14, 0))
								.availableSeats(200)
								.fareDto(FareDto.builder()
										.amount(250.00)
										.currency("USD")
										.build())
								.type(FlightType.DOMESTIC)
								.build()));
		newFlight = FlightDto.builder()
				.id(1L)
				.flightNumber("ABC123")
				.departureAirport("JFK")
				.destinationAirport("LAX")
				.departureTime(LocalDateTime.of(2024, 3, 11, 11, 0))
				.arrivalTime(LocalDateTime.of(2024, 3, 11, 14, 0))
				.availableSeats(200)
				.fareDto(FareDto.builder()
						.amount(250.00)
						.currency("USD")
						.build())
				.type(FlightType.DOMESTIC)
				.build();

		BDDMockito.given(flightService.createRestFlight(BDDMockito.any(FlightDto.class)))
				.willReturn(newFlight);

		// Mock the behavior of deleteFlight to throw FlightNotFoundException
		BDDMockito.willThrow(FlightNotFoundException.class)
				.given(flightService)
				.deleteFlight(1L);

		BDDMockito.doNothing()
				.when(flightService)
				.deleteFlight(2L);

	}

	@Test
	void testGetAllFlights() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.get("/api/flights")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status()
						.isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
						.value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].flightNumber")
						.value("ABC123"));
	}

	@Test
	void testCreateFlight() throws Exception {
		String jsonFlight = objectMapper.writeValueAsString(newFlight);
		this.mvc.perform(MockMvcRequestBuilders.post("/api/flights")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonFlight))
				.andExpect(MockMvcResultMatchers.status()
						.isCreated())
				.andExpect(MockMvcResultMatchers.header()
						.string("Location", "http://localhost/api/flights/1"));
	}

	@Test
	void testDeleteFlightWithException() throws Exception {
		// Perform DELETE request to delete a flight
		mvc.perform(MockMvcRequestBuilders.delete("/api/flights/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status()
						.isBadRequest());
	}

	@Test
	void testSuccessfulDeleteFlight() throws Exception {
		// Perform DELETE request to delete a flight
		mvc.perform(MockMvcRequestBuilders.delete("/api/flights/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status()
						.isNoContent());
	}

}

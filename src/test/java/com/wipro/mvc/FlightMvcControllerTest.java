package com.wipro.mvc;

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

import com.wipro.controllers.FlightController;
import com.wipro.dto.FareDto;
import com.wipro.dto.FlightDto;
import com.wipro.model.FlightType;
import com.wipro.service.FlightService;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(controllers = { FlightController.class })
public class FlightMvcControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private FlightService flightService;

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

		BDDMockito.willDoNothing()
				.given(flightService)
				.createFlight(BDDMockito.any(FlightDto.class));

	}

	@Test
	void testGetAllFlights() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.get("/flights")
				.accept(MediaType.TEXT_HTML))
				.andExpect(MockMvcResultMatchers.status()
						.isOk())
				.andExpect(MockMvcResultMatchers.view()
						.name("flight_list"))
				.andExpect(MockMvcResultMatchers.model()
						.attribute("flights", Matchers.hasSize(2)));
	}

	@Test
	void testCreateFlight() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/flights")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("flightNumber", "ABC123")
				.param("departureAirport", "JFK")
				.param("destinationAirport", "LAX")
				.param("departureTime", "2024-03-10T10:00")
				.param("arrivalTime", "2024-03-10T13:00")
				.param("availableSeats", "150")
				.param("fare.amount", "200.00")
				.param("fare.currency", "USD")
				.param("type", "DOMESTIC"))
				.andExpect(MockMvcResultMatchers.status()
						.is3xxRedirection());
	}

}

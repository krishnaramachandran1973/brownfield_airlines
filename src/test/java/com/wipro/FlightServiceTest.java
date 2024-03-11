package com.wipro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.wipro.dto.FlightDto;
import com.wipro.exceptions.FlightNotFoundException;
import com.wipro.model.Fare;
import com.wipro.model.Flight;
import com.wipro.model.FlightType;
import com.wipro.repository.FlightRepository;
import com.wipro.service.FlightService;

@TestInstance(Lifecycle.PER_CLASS)
//@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class FlightServiceTest {

	// @Mock
	@MockBean
	FlightRepository flightRepository;

	// @InjectMocks
	@Autowired
	FlightService flightService;

	String exceptionMsg = "No flight with id 10 found for deletion";

	@BeforeAll
	void setup() {
		// MockitoAnnotations.openMocks(this);
		given(flightRepository.findAll()).willReturn(Arrays.asList(Flight.builder()
				.id(1L)
				.flightNumber("ABC123")
				.departureAirport("JFK")
				.destinationAirport("LAX")
				.departureTime(LocalDateTime.of(2024, 3, 10, 10, 0))
				.arrivalTime(LocalDateTime.of(2024, 3, 10, 13, 0))
				.availableSeats(150)
				.fare(Fare.builder()
						.amount(200.00)
						.currency("USD")
						.build())
				.type(FlightType.DOMESTIC)
				.build(),
				Flight.builder()
						.id(2L)
						.flightNumber("DEF456")
						.departureAirport("LAX")
						.destinationAirport("JFK")
						.departureTime(LocalDateTime.of(2024, 3, 11, 11, 0))
						.arrivalTime(LocalDateTime.of(2024, 3, 11, 14, 0))
						.availableSeats(200)
						.fare(Fare.builder()
								.amount(250.00)
								.currency("USD")
								.build())
						.type(FlightType.DOMESTIC)
						.build()));

		given(flightRepository.findById(10L)).willThrow(new FlightNotFoundException(exceptionMsg));

	}

	@Test
	void shouldGetAllFlights() {
		List<FlightDto> flights = flightService.getAllFlights();
		assertThat(flights.size()).isEqualTo(2);
	}

	@Test()
	void shouldThrowErrorWhenDeletingFlight() {
		FlightNotFoundException exp = Assertions.assertThrows(FlightNotFoundException.class,
				() -> flightService.deleteFlight(10L));
		Assertions.assertEquals(exp.getMessage(), exceptionMsg);
	}

}

package com.wipro.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.wipro.model.Fare;
import com.wipro.model.Flight;
import com.wipro.repository.FlightRepository;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RepositoryTest {

	@Autowired
	private FlightRepository repository;

	@BeforeEach
	void setup() {
		Flight flight = Flight.builder()
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
				.build();
		repository.save(flight);
	}

	@Test
	public void testSearchFlight() {
		List<Flight> flights = repository.findByDepartureAirportAndDestinationAirport("JFK", "LAX");
		assertTrue(flights.size() > 0);
	}

}

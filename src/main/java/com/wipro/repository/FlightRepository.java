package com.wipro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wipro.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	@EntityGraph(value = "Flight.fare", type = EntityGraphType.LOAD)
	List<Flight> findAll();

	List<Flight> findByDepartureAirportAndDestinationAirport(String departureAirport, String destinationAirport);

	@Query("SELECT f FROM Flight f WHERE f.departureAirport = ?1")
	List<Flight> findByDepartureAirport(String departureAirport);
}

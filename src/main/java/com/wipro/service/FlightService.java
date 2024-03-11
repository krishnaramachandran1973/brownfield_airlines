package com.wipro.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wipro.dto.FlightDto;
import com.wipro.exceptions.FlightNotFoundException;
import com.wipro.mapper.DataMapper;
import com.wipro.model.Flight;
import com.wipro.repository.FlightRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlightService {

	private final FlightRepository flightRepository;

	public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	public List<FlightDto> getAllFlights() {
		return flightRepository.findAll()
				.stream()
				.map(DataMapper::toFlightDto)
				.collect(toList());
	}

	public void createFlight(FlightDto flightDto) {
		Flight flight = DataMapper.toFlight(flightDto);
		this.flightRepository.save(flight);
	}

	public void deleteFlight(Long id) {
		Optional<Flight> OptFlight = this.flightRepository.findById(id);
		Flight flight = OptFlight
				.orElseThrow(() -> new FlightNotFoundException("No flight with id " + id + " found for deletion"));
		this.flightRepository.deleteById(id);

	}

	public FlightDto getFlightById(Long id) {
		Optional<Flight> OptFlight = this.flightRepository.findById(id);
		Flight flight = OptFlight.orElseThrow(() -> new FlightNotFoundException("No flight with id " + id + " found"));
		return DataMapper.toFlightDto(flight);
	}

	public FlightDto createRestFlight(FlightDto flightDto) {
		Flight flight = DataMapper.toFlight(flightDto);
		return DataMapper.toFlightDto(this.flightRepository.save(flight));
	}

	public List<FlightDto> getFlightsByPage(int pageNumber, int pageSize) {
		List<FlightDto> flights = this.flightRepository.findAll(PageRequest.of(pageNumber, pageSize))
				.stream()
				.map(DataMapper::toFlightDto)
				.collect(toList());
		return flights;
	}

	public List<FlightDto> getFlightsByPageable(Pageable pageable) {
		List<FlightDto> flights = this.flightRepository.findAll(pageable)
				.stream()
				.map(DataMapper::toFlightDto)
				.collect(toList());
		return flights;
	}

	public List<FlightDto> getFlightByDepartureAirport(String departureAirport) {
		return this.flightRepository.findByDepartureAirport(departureAirport)
				.stream()
				.map(DataMapper::toFlightDto)
				.collect(toList());
	}
}

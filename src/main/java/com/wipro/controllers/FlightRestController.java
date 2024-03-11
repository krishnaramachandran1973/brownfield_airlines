package com.wipro.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wipro.common.Util;
import com.wipro.dto.FlightDto;
import com.wipro.errors.FlightValidationError;
import com.wipro.exceptions.FlightNotFoundException;
import com.wipro.service.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "FlightRestController", description = "Flight API.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/flights")
public class FlightRestController {

	private final FlightService flightService;

	@Operation(summary = "${api.flight-service.get-flights.summary}", description = "${api.flight-service.get-flights.description}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "${api.flight-service.get-flights.ok}", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "204", description = "${api.flight-service.get-flights.nocontent}") }

	)
	@GetMapping
	public ResponseEntity<?> getFlights() {
		// Call the FlightService to fetch flights
		List<FlightDto> flights = flightService.getAllFlights();
		if (flights.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.build();
		} else {
			return ResponseEntity.ok(flights);
		}
	}

	@GetMapping("/{id}")
	public FlightDto findFlightById(@PathVariable @Parameter(description = "${api.find-by-id.param.desc}") Long id) {
		// Get the Flight for a given id
		return flightService.getFlightById(id);
	}
	
	@GetMapping("/by-da")
	public List<FlightDto> findFlightByDepartureAirport(@RequestParam(name = "da") String departureAirport) {
		// Get the Flight for a given id
		return flightService.getFlightByDepartureAirport(departureAirport);
	}

	@PostMapping
	public ResponseEntity<?> createFlight(@Valid @RequestBody FlightDto flightDto, Errors errors) {
		if (errors.hasErrors()) {
			FlightValidationError error = Util.buildError(errors, "Validation failed");
			return ResponseEntity.badRequest()
					.body(error);// Return the error object if validation fails
		}
		FlightDto createdFlight = flightService.createRestFlight(flightDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdFlight.getId())
				.toUri();
		return ResponseEntity.created(location)
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
		// Call the service method to delete the flight
		flightService.deleteFlight(id);
		return ResponseEntity.noContent()
				.build();
	}

	@GetMapping("/paginate/{pageNumber}/{pageSize}")
	public List<FlightDto> getFlightsByPage(@PathVariable int pageNumber, @PathVariable int pageSize) {
		return flightService.getFlightsByPage(pageNumber, pageSize);
	}

	@GetMapping("/with-paginate-annotation")
	public List<FlightDto> getFlightsByAnnotation(
			@PageableDefault(size = 3, sort = "departureAirport") /*
																	 * @Parameter( hidden = true)
																	 */ Pageable pageable) {
		return flightService.getFlightsByPageable(pageable);
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public FlightValidationError handler(FlightNotFoundException exception) {
		return FlightValidationError.builder()
				.errorMessage(exception.getMessage())
				.build();
	}
}

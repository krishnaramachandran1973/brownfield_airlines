package com.wipro.dto;

import java.time.LocalDateTime;

import com.wipro.model.FlightType;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
		title = "Flight Entity",
		description = "Represents a flight in the system",
		accessMode = AccessMode.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDto {
	@Schema(
			description = "Unique identifier for the flight",
			accessMode = AccessMode.READ_ONLY)
	private Long id;

	@Schema(
			description = "Flight number")
	@NotNull
	@Size(
			min = 3,
			max = 15,
			message = "Title must be minimum 3 characters, and maximum 15 characters long")
	private String flightNumber;

	@Schema(
			description = "Departure airport code")
	@NotNull
	@NotEmpty
	private String departureAirport;

	@Schema(
			description = "Destination airport code")
	@NotNull
	@NotEmpty
	private String destinationAirport;
	@NotNull
	private LocalDateTime departureTime;
	@NotNull
	private LocalDateTime arrivalTime;
	@NotNull
	private int availableSeats;
	@Default
	private FareDto fareDto = new FareDto();
	@NotNull
	private FlightType type;
}

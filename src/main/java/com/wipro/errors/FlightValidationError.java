package com.wipro.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(
		title = "Validation and Exception mesages",
		description = "Represents a message from the system for validation errors and exceptions",
		accessMode = AccessMode.READ_ONLY)
@RequiredArgsConstructor
@Builder
public class FlightValidationError {

	@Schema(
			description = "The error message")
	@Getter
	private final String errorMessage;

	@Schema(
			description = "The list of validation failures, if present")
	@Default
	@Getter
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<String> errors = new ArrayList<>();

	public void addValidationError(String error) {
		errors.add(error);
	}
}

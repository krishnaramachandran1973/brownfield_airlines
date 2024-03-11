package com.wipro.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;

import com.wipro.errors.FlightValidationError;

public class Util {
	public static FlightValidationError buildError(Errors errors, String message) {
		List<String> fes = new ArrayList<>();
		
		errors.getFieldErrors()
				.forEach(fe -> fes.add(fe.getField() + " " + fe.getDefaultMessage()));
		
		FlightValidationError flightValidationError = FlightValidationError.builder()
				.errorMessage(message + " " + errors.getErrorCount() + " errors")
				.errors(fes)
				.build();
		return flightValidationError;
	}

}

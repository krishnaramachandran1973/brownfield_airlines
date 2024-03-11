package com.wipro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
		title = "Fare Entity",
		description = "Represents the fare for a flight",
		accessMode = AccessMode.READ_WRITE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FareDto {
	private Long id;
	private double amount;
	private String currency;
}

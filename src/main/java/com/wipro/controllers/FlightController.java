package com.wipro.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wipro.dto.FlightDto;
import com.wipro.service.FlightService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FlightController {

	private final FlightService flightService;

	@GetMapping("/flights")
	public String getFlights(Model model) {
		// Call the FlightService to fetch flights
		List<FlightDto> flights = flightService.getAllFlights();

		// Add fetched flights to the model
		model.addAttribute("flights", flights);

		// Return the view name
		return "flight_list";
	}

	@GetMapping("/flights/new")
	public String showFlightForm(Model model) {
		model.addAttribute("flight", new FlightDto());
		return "create_flight";
	}

	@PostMapping("/flights")
	public String createFlight(@Valid @ModelAttribute("flight") FlightDto flightDto, BindingResult result) {
		if (result.hasErrors()) {
			return "create_flight"; // Return to the form if validation fails
		}
		flightService.createFlight(flightDto);
		return "redirect:/flights"; // Redirect to the flight list page after successful creation
	}

	@DeleteMapping("/flights")
	public String deleteFlight(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String id = request.getParameter("id");
		// Call the service method to delete the flight
		flightService.deleteFlight(Long.parseLong(id));

		// Add a flash attribute to display a success message
		redirectAttributes.addFlashAttribute("successMessage", "Flight deleted successfully.");

		// Redirect back to the flight list page
		return "redirect:/flights";
	}
}

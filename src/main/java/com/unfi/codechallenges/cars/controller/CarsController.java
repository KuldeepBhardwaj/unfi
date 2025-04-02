package com.unfi.codechallenges.cars.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.unfi.codechallenges.cars.dto.CarDto;
import com.unfi.codechallenges.cars.exception.CarNotFoundException;
import com.unfi.codechallenges.cars.exception.ErrorResponse;
import com.unfi.codechallenges.cars.service.CarService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cars")
public class CarsController {

    private final CarService carService;

    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        log.info("Getting all active cars");
        return ResponseEntity.ok(carService.getAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createCar(@Valid @RequestBody CarDto car) {
		log.info("Creaing a car {}", car);
		try {
			return ResponseEntity.ok(carService.createCar(car));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Internal Error Occured", e.getMessage())); // Catch other unexpected errors

		}
	}

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable Long id, @Valid @RequestBody CarDto car) {
    	log.info("Updating car witd id : {}", id);
        try {
            car.setId(id); // Ensure the ID matches the DTO
            CarDto updatedCar = carService.update(car);
            return ResponseEntity.ok(updatedCar);
        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("CAR_NOT_FOUND", e.getMessage())); // Return 404 if car not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Error Occured", e.getMessage())); // Catch other unexpected errors
        }
    }

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
		log.info("Deleting car with id: {}", id);
        try {
            CarDto carDto = CarDto.builder().id(id).build(); // Create DTO with ID
            carService.delete(carDto);
            return ResponseEntity.noContent().build();
        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("CAR_NOT_FOUND", e.getMessage())); // Return 404 if car not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Error Occured", e.getMessage())); // Catch other unexpected errors
        }
	}

    // Custom exception handler for validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
	    String errorMessage = e.getBindingResult().getFieldErrors()
	                          .stream()
	                          .map(error -> error.getField() + ": " + error.getDefaultMessage())
	                          .collect(Collectors.joining(", "));
	    log.error("Validation error: {}", errorMessage);
	    return ResponseEntity.badRequest().body(errorMessage);
	}

}
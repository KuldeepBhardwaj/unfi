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
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CarDto car) {
    	log.info("Creaing a car {}", car);
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @Valid @RequestBody CarDto car) {
    	log.info("Updating car with id : {}", id);
    	car.setId(id); // Ensure the ID matches the DTO
        return ResponseEntity.ok(carService.update(car));
    }

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
		log.info("Deleting car with id: {}", id);
		CarDto carDto = CarDto.builder().id(id).build();
		carService.delete(carDto);
		return ResponseEntity.noContent().build();
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
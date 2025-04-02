package com.unfi.codechallenges.cars.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.unfi.codechallenges.cars.dto.CarDto;
import com.unfi.codechallenges.cars.dto.CarDto.CarDtoBuilder;
import com.unfi.codechallenges.cars.entity.Car;
import com.unfi.codechallenges.cars.exception.CarNotFoundException;
import com.unfi.codechallenges.cars.repository.CarRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;
    private static final String CAR_NOT_FOUND_MESSAGE = "Car not found with ID: ";

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarDto createCar(CarDto car) {
    	Car newCar = Car.builder().make(car.getMake()).model(car.getModel()).year(car.getYear()).vin(car.getVin())
				.isActive(Boolean.TRUE).build();
        log.info("Creating car : {}" , car);
        var createdCar = carRepository.save(newCar);
        log.info("Created car with id: {}", createdCar.getId());
        return mapToCarDto(createdCar)
                .build();
    }

	public CarDto update(CarDto car) {
		Car foundCar = carRepository.findById(car.getId())
				.orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND_MESSAGE + car.getId()));
		foundCar.setMake(car.getMake());
		foundCar.setModel(car.getModel());
		foundCar.setYear(car.getYear());
		foundCar.setVin(car.getVin());
		foundCar.setIsActive(true);
		var updatedCar = carRepository.save(foundCar);
		log.info("Updated car {}", updatedCar);
		return mapToCarDto(updatedCar).build();
	}

	public void delete(CarDto car) {
		Car foundCar = carRepository.findById(car.getId())
				.orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND_MESSAGE + car.getId()));
		log.info("Soft deleting car with id: {}", foundCar.getId());
		foundCar.setIsActive(false);
		carRepository.save(foundCar);
	}

    public List<CarDto> getAll() {
    	return carRepository.findAllByIsActiveTrue().stream()
    		    .map(car -> mapToCarDto(car)
    		        .build())
    		    .collect(Collectors.toList());
    }
    
    private CarDtoBuilder mapToCarDto(Car updatedCar) {
		return CarDto.builder().id(updatedCar.getId()).make(updatedCar.getMake()).model(updatedCar.getModel())
				.year(updatedCar.getYear()).vin(updatedCar.getVin());
	}
}

package com.unfi.codechallenges.cars.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.unfi.codechallenges.cars.dto.CarDto;
import com.unfi.codechallenges.cars.entity.Car;
import com.unfi.codechallenges.cars.exception.CarNotFoundException;
import com.unfi.codechallenges.cars.repository.CarRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarDto createCar(CarDto car) {
		Car newCar = Car.builder().make(car.getMake()).model(car.getModel()).year(car.getYear()).vin(car.getVin())
				.isActive(Boolean.TRUE).build();
        log.info("Creating car");
        var createdCar = carRepository.save(newCar);
        log.info("Created car with id: {}", createdCar.getId());
        return CarDto.builder()
                .id(createdCar.getId())
                .make(createdCar.getMake())
                .model(createdCar.getModel())
                .year(createdCar.getYear())
                .vin(createdCar.getVin())
                .build();
    }

    public CarDto update(CarDto car) {
    	Car foundCar = carRepository.findById(car.getId())
				.orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + car.getId()));
        if (null != foundCar) {
            foundCar.setMake(car.getMake());
            foundCar.setModel(car.getModel());
            foundCar.setYear(car.getYear());
            foundCar.setVin(car.getVin());
            foundCar.setIsActive(true);
            var updatedCar = carRepository.save(foundCar);
            log.info("Updated car {}", updatedCar);
            return CarDto.builder()
                    .id(updatedCar.getId())
                    .make(updatedCar.getMake())
                    .model(updatedCar.getModel())
                    .year(updatedCar.getYear())
                    .vin(updatedCar.getVin())
                    .build();
        } 
        return null;
    }

	public void delete(CarDto car) {

		Car optionalCar = carRepository.findById(car.getId())
				.orElseThrow(() -> new CarNotFoundException("Car not found with ID: " + car.getId()));
		if (null != optionalCar) {
			log.info("Soft deleting car with id: {}", optionalCar.getId());
			optionalCar.setIsActive(false);
			carRepository.softDeleteById(car.getId());
		} 
	}

    public List<CarDto> getAll() {
    	return carRepository.findAllByIsActiveTrue().stream()
    		    .map(car -> CarDto.builder()
    		        .id(car.getId())
    		        .make(car.getMake())
    		        .model(car.getModel())
    		        .year(car.getYear())
    		        .vin(car.getVin())
    		        .build())
    		    .collect(Collectors.toList());

    }
    
    
    
   
}

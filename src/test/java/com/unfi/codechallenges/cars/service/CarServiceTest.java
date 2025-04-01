package com.unfi.codechallenges.cars.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unfi.codechallenges.cars.dto.CarDto;
import com.unfi.codechallenges.cars.entity.Car;
import com.unfi.codechallenges.cars.repository.CarRepository;

class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCar() {
        CarDto carDto = CarDto.builder()
            .make("Toyota")
            .model("Camry")
            .year("2020")
            .vin("1234567890")
            .build();

        Car car = Car.builder()
            .make("Toyota")
            .model("Camry")
            .year("2020")
            .vin("1234567890")
            .isActive(true)
            .build();

        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarDto result = carService.createCar(carDto);

        assertNotNull(result);
        assertEquals("Toyota", result.getMake());
        assertEquals("Camry", result.getModel());
        assertEquals("2020", result.getYear());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testUpdateCar() {
        CarDto carDto = CarDto.builder()
            .id(1L)
            .make("Honda")
            .model("Accord")
            .year("2021")
            .vin("9876543210")
            .build();

        Car car = Car.builder()
            .id(1L)
            .make("Toyota")
            .model("Camry")
            .year("2020")
            .vin("1234567890")
            .isActive(true)
            .build();

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarDto result = carService.update(carDto);

        assertNotNull(result);
        assertEquals("Honda", result.getMake());
        assertEquals("Accord", result.getModel());
        assertEquals("2021", result.getYear());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testDeleteCar() {
        Car car = Car.builder()
            .id(1L)
            .make("Toyota")
            .model("Camry")
            .year("2020")
            .vin("1234567890")
            .isActive(true)
            .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        CarDto carDto = CarDto.builder().id(1L).build();
        carService.delete(carDto);

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).softDeleteById(1L);
        assertFalse(car.getIsActive());
    }

    @Test
    void testGetAllCars() {
        Car car = Car.builder()
            .id(1L)
            .make("Toyota")
            .model("Camry")
            .year("2020")
            .vin("1234567890")
            .isActive(true)
            .build();

        when(carRepository.findAllByIsActiveTrue()).thenReturn(List.of(car));

        List<CarDto> result = carService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        verify(carRepository, times(1)).findAllByIsActiveTrue();
    }
    
    @Test
    void testUpdateCarNotFoundException() {
        CarDto carDto = CarDto.builder()
            .id(1L)
            .make("Honda")
            .model("Accord")
            .year("2021")
            .vin("9876543210")
            .build();

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> carService.update(carDto));

        assertEquals("Car not found with ID: 1", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void testDeleteCarNotFoundException() {
        CarDto carDto = CarDto.builder().id(1L).build();

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> carService.delete(carDto));

        assertEquals("Car not found with ID: 1", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, never()).save(any(Car.class));
    }

}
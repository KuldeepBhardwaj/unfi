package com.unfi.codechallenges.cars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unfi.codechallenges.cars.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    public List<Car> findAllByIsActiveTrue();
    
    @Modifying
    @Transactional
    @Query("UPDATE Car c SET c.isActive = false WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

}

package com.speedreadingapp.repository;

import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.entity.exercise.SpeedMeterResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeedMeterResultRepository extends JpaRepository<SpeedMeterResult, Long> {

    @Query("SELECT e FROM SpeedMeterResult e WHERE e.applicationUser.id = ?1")
    List<SpeedMeterResult> findAllByUserId(Long userId);

}

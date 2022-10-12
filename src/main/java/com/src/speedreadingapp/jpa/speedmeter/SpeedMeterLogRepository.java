package com.src.speedreadingapp.jpa.speedmeter;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SpeedMeterLogRepository extends JpaRepository<SpeedMeterLog, Long> {
}

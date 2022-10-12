package com.src.speedreadingapp.jpa.speedmeter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpeedMeterLogService {
    private final SpeedMeterLogRepository speedMeterLogRepository;

    public SpeedMeterLog saveSpeedMeterLogService(SpeedMeterLog log) {
        return speedMeterLogRepository.save(log);
    }
}

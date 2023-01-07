package com.src.speedreadingapp.jpa.speedmeter;


import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpeedMeterLogService {
    private final SpeedMeterLogRepository speedMeterLogRepository;
    private final AppUserService appUserService;

    @Transactional
    public SpeedMeterLog saveSpeedMeterLog(SpeedMeterLog log, Long userId) throws UsernameNotFoundException {
        SpeedMeterLog speedMeterLog = speedMeterLogRepository.save(log);
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        optionalAppUser.ifPresentOrElse(
                (value) -> {
                        value.getSpeedMeterLogs().add(speedMeterLog);
                        speedMeterLog.setAppUser(value);
                    },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );


        return speedMeterLog;
    }
}

package com.src.speedreadingapp.jpa.understandingmeter;

import com.src.speedreadingapp.course.UserProgress;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UnderstandingLevelLogService {
    private final UnderstandingLevelLogRepostitory understandingLevelLogRepostitory;
    private final AppUserService appUserService;

    @Transactional
    public UnderstandingLevelLog saveLog(Long userId, UnderstandingLevelLog understandingLevelLog) {
        UnderstandingLevelLog log = understandingLevelLogRepostitory.save(understandingLevelLog);
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        optionalAppUser.ifPresentOrElse(
                (value) -> {
                    value.setUnderstandingLevelLog(log);
                    log.setAppUser(value);
                },
                () -> {}
        );
        return log;
    }

    @Transactional
    public UnderstandingLevelLog getLog(Long userId) throws Exception {
        AppUser appUser;
        UnderstandingLevelLog understandingLevelLog;
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            understandingLevelLog = optionalAppUser.get().getUnderstandingLevelLog();
        return understandingLevelLog;
    }
}

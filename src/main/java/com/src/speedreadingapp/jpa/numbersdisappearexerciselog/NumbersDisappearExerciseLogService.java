package com.src.speedreadingapp.jpa.numbersdisappearexerciselog;


import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NumbersDisappearExerciseLogService {
    private final NumbersDisappearExerciseLogRepository numbersDisappearExerciseLogRepository;
    private final AppUserService appUserService;

    @Transactional
    public NumbersDisappearExerciseLog save(NumbersDisappearExerciseLog log, Long userId) throws UsernameNotFoundException {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new UsernameNotFoundException("User " + userId + "doesn't exist");
        AppUser appUser = optionalAppUser.get();

        NumbersDisappearExerciseLog numbersDisappearExerciseLog;
        if(appUser.getNumbersDisappearExerciseLog() == null) {
            numbersDisappearExerciseLog = numbersDisappearExerciseLogRepository.save(log);
            numbersDisappearExerciseLog.setAppUser(appUser);
            appUser.setNumbersDisappearExerciseLog(numbersDisappearExerciseLog);
        } else {
            numbersDisappearExerciseLog = appUser.getNumbersDisappearExerciseLog();
            numbersDisappearExerciseLog.setArray(log.getArray());
        }
        return numbersDisappearExerciseLog;
    }

    @Transactional
    public NumbersDisappearExerciseLog get(Long userId) throws UsernameNotFoundException {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new UsernameNotFoundException("User " + userId + "doesn't exist");
        AppUser appUser = optionalAppUser.get();
        return appUser.getNumbersDisappearExerciseLog();
    }
}

package com.src.speedreadingapp.jpa.columnnumberexerciselogs;


import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLog;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ColumnNumberExerciseLogService {
    private final ColumnNumberExerciseLogRepository columnNumberExerciseLogRepository;
    private final AppUserService appUserService;

    @Transactional
    public ColumnNumberExerciseLog save(ColumnNumberExerciseLog log, Long userId) throws Exception {
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User " + userId + "doesn't exist");
        AppUser appUser = optionalAppUser.get();

        ColumnNumberExerciseLog columnNumberExerciseLog;
        if(appUser.getColumnNumberExerciseLog() == null) {
            columnNumberExerciseLog = columnNumberExerciseLogRepository.save(log);
            columnNumberExerciseLog.setAppUser(appUser);
            appUser.setColumnNumberExerciseLog(columnNumberExerciseLog);
        } else {
            columnNumberExerciseLog = appUser.getColumnNumberExerciseLog();
            columnNumberExerciseLog.setArray(log.getArray());
        }

        return columnNumberExerciseLog;
    }

    @Transactional
    public ColumnNumberExerciseLog get(Long userId) throws Exception {
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User " + userId + "doesn't exist");
        AppUser appUser = optionalAppUser.get();
        return appUser.getColumnNumberExerciseLog();
    }
}

package com.src.speedreadingapp.jpa.schultzarraylogs;

import com.src.speedreadingapp.controlers.SchultzArrayLogsController;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
@RequiredArgsConstructor
public class SchultzArrayService {
    private final SchultzArrayLogRepository schultzArrayLogRepository;
    private final AppUserService appUserService;

    @Transactional
    public SchultzArrayLog putSchultzArrayLog(SchultzArrayLog l, AppUser user) throws Exception {
        SchultzArrayLog schultzArrayLog;
        if(user.getSchultzArrayLog() == null) {
            schultzArrayLog = schultzArrayLogRepository.save(l);
            user.setSchultzArrayLog(l);
        } else {
            schultzArrayLog = user.getSchultzArrayLog();
            schultzArrayLog.setArray(l.getArray());
        }

        return schultzArrayLog;
    }
}

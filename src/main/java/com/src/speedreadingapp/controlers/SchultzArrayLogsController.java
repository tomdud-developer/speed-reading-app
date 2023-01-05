package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLog;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/schultz-array-logs")
@RequiredArgsConstructor
public class SchultzArrayLogsController {

    private final AppUserService appUserService;
    private final SchultzArrayService schultzArrayLogsService;


    @PutMapping(value = "/save/{userId}")
    @CrossOrigin
    protected SchultzArrayLog putLogs(@RequestBody SchultzArrayLog schultzArrayLog, @PathVariable("userId") Long userId) throws Exception {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isPresent())
            return schultzArrayLogsService.putSchultzArrayLog(schultzArrayLog, optionalAppUser.get());
        else
            throw new RuntimeException("This user doesn't exsist in database!");
    }

    @GetMapping(value = "/get/{userId}")
    @CrossOrigin
    protected SchultzArrayLog getLogs(@PathVariable("userId") Long userId) throws Exception {
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isPresent()) {
            SchultzArrayLog arrayLog = optionalAppUser.get().getSchultzArrayLog();
            if(arrayLog == null) {
                arrayLog = schultzArrayLogsService.putSchultzArrayLog(
                        new SchultzArrayLog(),
                        optionalAppUser.get()
                );
            }
            return arrayLog;
        }
        else
            throw new RuntimeException("This user doesn't exsist in database!");
    }

}

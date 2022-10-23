package com.src.speedreadingapp.controlers;


import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/speed-meter-log")
@RequiredArgsConstructor
public class SpeedMeterLogController {
    private final SpeedMeterLogService speedMeterLogService;
    private final AppUserService appUserService;

    @PostMapping(value = "/save")
    @CrossOrigin
    protected SpeedMeterLog saveLog(@RequestBody SpeedMeterLog speedMeterLog) {
        return speedMeterLogService.saveSpeedMeterLog(speedMeterLog);
    }

    @GetMapping(value = "/get/{userId}")
    @CrossOrigin
    protected List<SpeedMeterLog> getLogs(@PathVariable("userId") Long userId) {
        Set<SpeedMeterLog> logs;
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isPresent())
            logs = optionalAppUser.get().getSpeedMeterLogs();
        else
            throw new RuntimeException("This user doesn't exsist in database!");
        return logs.stream().sorted((log1, log2) -> {
            LocalDateTime log1Date = log1.getDate();
            LocalDateTime log2Date = log2.getDate();
            return log1Date.compareTo(log2Date);
        }).toList();
    }

    @GetMapping(value = "/get-latest/{userId}")
    @CrossOrigin
    protected SpeedMeterLog getLatestLog(@PathVariable("userId") Long userId) {
        Set<SpeedMeterLog> logs;
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isPresent())
            logs = optionalAppUser.get().getSpeedMeterLogs();
        else
            throw new RuntimeException("This user doesn't exsist in database!");
        return logs.stream().sorted((log1, log2) -> {
            LocalDateTime log1Date = log1.getDate();
            LocalDateTime log2Date = log2.getDate();
            return log1Date.compareTo(log2Date);
        }).skip(logs.size() - 1).findFirst().get();
    }



}

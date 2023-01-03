package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLog;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLogService;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayLog;
import com.src.speedreadingapp.jpa.schultzarraylogs.SchultzArrayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/column-numbers-logs")
@RequiredArgsConstructor
public class ColumnNumberExerciseLogController {


    private final ColumnNumberExerciseLogService columnNumberExerciseLogService;


    @PutMapping(value = "/save/{userId}")
    @CrossOrigin
    protected ColumnNumberExerciseLog putLogs(@RequestBody ColumnNumberExerciseLog columnNumberExerciseLog, @PathVariable("userId") Long userId) throws Exception {
        return columnNumberExerciseLogService.save(columnNumberExerciseLog, userId);
    }

    @GetMapping(value = "/get/{userId}")
    @CrossOrigin
    protected ColumnNumberExerciseLog getLogs(@PathVariable("userId") Long userId) throws Exception {
        return columnNumberExerciseLogService.get(userId);
    }



}

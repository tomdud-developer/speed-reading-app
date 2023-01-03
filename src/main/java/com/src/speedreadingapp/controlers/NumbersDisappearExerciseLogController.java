package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLog;
import com.src.speedreadingapp.jpa.columnnumberexerciselogs.ColumnNumberExerciseLogService;
import com.src.speedreadingapp.jpa.numbersdisappearexerciselog.NumbersDisappearExerciseLog;
import com.src.speedreadingapp.jpa.numbersdisappearexerciselog.NumbersDisappearExerciseLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/numbers-disappear-logs")
@RequiredArgsConstructor
public class NumbersDisappearExerciseLogController {


    private final NumbersDisappearExerciseLogService numbersDisappearExerciseLogService;


    @PutMapping(value = "/save/{userId}")
    @CrossOrigin
    protected NumbersDisappearExerciseLog putLogs(@RequestBody NumbersDisappearExerciseLog numbersDisappearExerciseLog, @PathVariable("userId") Long userId) throws Exception {
        return numbersDisappearExerciseLogService.save(numbersDisappearExerciseLog, userId);
    }

    @GetMapping(value = "/get/{userId}")
    @CrossOrigin
    protected NumbersDisappearExerciseLog getLogs(@PathVariable("userId") Long userId) throws Exception {
        return numbersDisappearExerciseLogService.get(userId);
    }


}

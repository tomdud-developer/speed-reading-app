package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLog;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLogService;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelQuestion;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelText;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelTextAndQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/understanding-meter")
@RequiredArgsConstructor
public class UnderstandingMeterLogController {

    private final UnderstandingLevelLogService understandingLevelLogService;
    private final UnderstandingLevelTextAndQuestionService understandingLevelTextAndQuestionService;

    @PostMapping(value = "/save/{userId}")
    @CrossOrigin
    protected UnderstandingLevelLog saveLog(@PathVariable Long userId, @RequestBody UnderstandingLevelLog understandingLevelLog) {
        return understandingLevelLogService.saveLog(userId, understandingLevelLog);
    }

    @GetMapping(value = "/get/{userId}")
    @CrossOrigin
    protected UnderstandingLevelLog getLog(@PathVariable Long userId) throws Exception {
        return understandingLevelLogService.getLog(userId);
    }

    @GetMapping(value = "/text/{number}")
    @CrossOrigin
    protected String getExercise(@PathVariable Long number) {
        return understandingLevelTextAndQuestionService.getTextNumberX(number).getContent();
    }

    @GetMapping(value = "/questions/{number}")
    @CrossOrigin
    protected List<UnderstandingLevelQuestion> getQuestionsToTextNumberX(@PathVariable Integer number) {
        return understandingLevelTextAndQuestionService.getQuestionsToTextNumberX(number);
    }
}

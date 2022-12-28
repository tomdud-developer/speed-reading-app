package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.course.UserProgress;
import com.src.speedreadingapp.course.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user-progress")
@RequiredArgsConstructor
public class UserProgressController {
    private final UserProgressService userProgressService;

    @GetMapping(value = "/get/{userId}")
    public UserProgress getUserProgress(@PathVariable Long userId) throws Exception {
        return userProgressService.getUserProgressByUserId(userId);
    }
}

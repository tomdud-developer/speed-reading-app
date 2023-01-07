package com.src.speedreadingapp.jpa.course;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;
    private final AppUserService appUserService;

    @Transactional
    public UserProgress createNewAndSaveUserProgress(AppUser appUser) {
        UserProgress userProgress = new UserProgress(
                null,
                false,
                1,
                0,
                null,
                appUser
        );

        return userProgressRepository.save(userProgress);
    }


    public UserProgress getUserProgressByUserId(Long userId) throws Exception{
        AppUser appUser;
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            appUser = optionalAppUser.get();

        return appUser.getUserProgress();
    }

    @Transactional
    public Integer confirmExercise(Long userId, Integer exerciseNumber) throws Exception{
        AppUser appUser;
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            appUser = optionalAppUser.get();
        appUser.getUserProgress().setFinishedExercise(exerciseNumber);
        return exerciseNumber;
    }

    @Transactional
    public Integer confirmSession(Long userId, Integer sessionNumber) throws Exception {
        AppUser appUser;
        UserProgress userProgress;
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            userProgress = optionalAppUser.get().getUserProgress();
        Integer currentSession = userProgress.getCurrentSessionNumber();
        Integer nextSession = currentSession + 1;

        if(sessionNumber + 1 != nextSession)
            throw new Exception("Mismatch beetwen currentSession and nextSession value");
        else {
            userProgress.setCurrentSessionNumber(nextSession);
            userProgress.setFinishedExercise(0);
        }


        return nextSession;
    }

    @Transactional
    public void resetProgress(Long userId) throws Exception {
        AppUser appUser;
        UserProgress userProgress;
        Optional<AppUser> optionalAppUser = appUserService.findById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            userProgress = optionalAppUser.get().getUserProgress();

        userProgress.setCurrentSessionNumber(1);
        userProgress.setFinishedExercise(0);
    }
}

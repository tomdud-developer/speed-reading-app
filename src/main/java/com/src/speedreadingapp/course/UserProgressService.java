package com.src.speedreadingapp.course;

import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;
    private final AppUserService appUserService;

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
        Optional<AppUser> optionalAppUser = appUserService.finById(userId);
        if(optionalAppUser.isEmpty())
            throw new Exception("User with this id doesn't exist!");
        else
            appUser = optionalAppUser.get();

        return appUser.getUserProgress();
    }
}

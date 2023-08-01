package com.speedreadingapp.service;


import com.speedreadingapp.dto.DisappearNumbersResultRequestDTO;
import com.speedreadingapp.dto.DisappearNumbersResultResponseDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.repository.DisappearNumbersResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class ExerciseResultService {

    private final DisappearNumbersResultRepository disappearNumbersResultRepository;
    private final ApplicationUserService applicationUserService;

    @Transactional
    public DisappearNumbersResultResponseDTO saveDisappearNumbersResult(
            DisappearNumbersResultRequestDTO disappearNumbersResultRequestDTO) {

        DisappearNumbersResult disappearNumbersResult = new DisappearNumbersResult(
                0L,
                disappearNumbersResultRequestDTO.getDifficultyLevel(),
                disappearNumbersResultRequestDTO.getTimeResultInSeconds(),
                LocalDateTime.now(),
                getUserFromAuthContext()
        );

        disappearNumbersResult = disappearNumbersResultRepository.save(disappearNumbersResult);

        return ValueMapper.convertEntityToResponseDTO(disappearNumbersResult);
    }

    @Transactional
    public List<DisappearNumbersResultResponseDTO> getDisappearNumbersResults() {

        ApplicationUser applicationUser = getUserFromAuthContext();

        List<DisappearNumbersResult> disappearNumbersResultList =
                disappearNumbersResultRepository.findAllByUserId(applicationUser.getId());

        return disappearNumbersResultList.stream().map(ValueMapper::convertEntityToResponseDTO).toList();
    }



    private ApplicationUser getUserFromAuthContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof User user)
            email = user.getUsername();
        else if (principal instanceof String string)
            email = string;
        else
            throw new SecurityException("Unsupported princiapl in context");

        return applicationUserService.getApplicationUser(email);
    }

}

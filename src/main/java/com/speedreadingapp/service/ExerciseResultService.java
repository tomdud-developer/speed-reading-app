package com.speedreadingapp.service;


import com.speedreadingapp.dto.exercise.DisappearNumbersResultRequestDTO;
import com.speedreadingapp.dto.exercise.DisappearNumbersResultResponseDTO;
import com.speedreadingapp.dto.exercise.SpeedMeterResultRequestDTO;
import com.speedreadingapp.dto.exercise.SpeedMeterResultResponseDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.entity.exercise.SpeedMeterResult;
import com.speedreadingapp.repository.DisappearNumbersResultRepository;
import com.speedreadingapp.repository.SpeedMeterResultRepository;
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
    private final SpeedMeterResultRepository speedMeterResultRepository;
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

    @Transactional(readOnly = true)
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
            throw new SecurityException("Unsupported principal in context");

        return applicationUserService.getApplicationUser(email);
    }

    @Transactional
    public SpeedMeterResultResponseDTO saveSpeedMeterResult(SpeedMeterResultRequestDTO speedMeterResultRequestDTO) {
        SpeedMeterResult speedMeterResult = new SpeedMeterResult(
                0L,
                speedMeterResultRequestDTO.getWordsPerMinute(),
                speedMeterResultRequestDTO.getNumberOfWords(),
                LocalDateTime.now(),
                getUserFromAuthContext()
        );

        speedMeterResult = speedMeterResultRepository.save(speedMeterResult);

        return ValueMapper.convertEntityToResponseDTO(speedMeterResult);
    }

    @Transactional(readOnly = true)
    public List<SpeedMeterResultResponseDTO> getSpeedMeterResults() {

        ApplicationUser applicationUser = getUserFromAuthContext();

        List<SpeedMeterResult> speedMeterResultResponseDTOList =
                speedMeterResultRepository.findAllByUserId(applicationUser.getId());

        return speedMeterResultResponseDTOList.stream().map(ValueMapper::convertEntityToResponseDTO).toList();
    }


}

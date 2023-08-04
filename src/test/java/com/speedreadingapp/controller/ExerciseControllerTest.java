package com.speedreadingapp.controller;

import com.speedreadingapp.dto.exercise.DisappearNumbersResultRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.repository.ApplicationUserRepository;
import com.speedreadingapp.repository.DisappearNumbersResultRepository;
import com.speedreadingapp.util.DifficultyLevel;
import com.speedreadingapp.util.MockApplicationUserFactory;
import com.speedreadingapp.util.ObjectToJsonAsStringConverter;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExerciseControllerTest {

    private static final String EXERCISES_ENDPOINT_URL = "/api/v2/exercises-results";
    private static final String DISAPPEAR_NUMBERS_RESULTS_URL = EXERCISES_ENDPOINT_URL + "/disappear-numbers";
    private static final String SPEED_METER_RESULTS_URL = EXERCISES_ENDPOINT_URL + "/speed-meter";

    @InjectMocks
    private ExercisesResultsController exercisesResultsController;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @MockBean
    private DisappearNumbersResultRepository disappearNumbersResultRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockApplicationUserFactory mockApplicationUserFactory;


    @Before("")
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.exercisesResultsController).build();
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void saveDisappearsNumbersResult() throws Exception {

        //given
        DisappearNumbersResultRequestDTO disappearNumbersResultRequestDTO =
                DisappearNumbersResultRequestDTO.builder()
                        .timeResultInSeconds(25.4)
                        .difficultyLevel(DifficultyLevel.EASY)
                        .build();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        DisappearNumbersResult disappearNumbersResult = new DisappearNumbersResult(
                5L,
                disappearNumbersResultRequestDTO.getDifficultyLevel(),
                disappearNumbersResultRequestDTO.getTimeResultInSeconds(),
                LocalDateTime.now(),
                applicationUser
        );

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));
        when(disappearNumbersResultRepository.save(any())).thenReturn(disappearNumbersResult);


        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(DISAPPEAR_NUMBERS_RESULTS_URL)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(disappearNumbersResultRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.results.timeResultInSeconds")
                                .value(25.4)
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void saveDisappearsNumbersResultFailed() throws Exception {

        //given
        DisappearNumbersResultRequestDTO disappearNumbersResultRequestDTO =
                DisappearNumbersResultRequestDTO.builder()
                        .timeResultInSeconds(-1.4)
                        .difficultyLevel(DifficultyLevel.EASY)
                        .build();

        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        DisappearNumbersResult disappearNumbersResult = new DisappearNumbersResult(
                5L,
                disappearNumbersResultRequestDTO.getDifficultyLevel(),
                disappearNumbersResultRequestDTO.getTimeResultInSeconds(),
                LocalDateTime.now(),
                applicationUser
        );

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));
        when(disappearNumbersResultRepository.save(any())).thenReturn(disappearNumbersResult);


        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(DISAPPEAR_NUMBERS_RESULTS_URL)
                        .content(Objects.requireNonNull(
                                ObjectToJsonAsStringConverter.convert(disappearNumbersResultRequestDTO))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.errors[0].field")
                                .value("timeResultInSeconds")
                );
    }

    @Test
    @WithMockUser(value = "test@test.com")
    void getAllDisappearsNumbersResult() throws Exception {

        //given
        ApplicationUser applicationUser = mockApplicationUserFactory.getMockUserWithHashedPassword();

        DisappearNumbersResult disappearNumbersResult1 = new DisappearNumbersResult(
                5L,
                DifficultyLevel.HARD,
                21.4,
                LocalDateTime.now(),
                applicationUser
        );

        DisappearNumbersResult disappearNumbersResult2 = new DisappearNumbersResult(
                6L,
                DifficultyLevel.CHALLENGING,
                27.3,
                LocalDateTime.now(),
                applicationUser
        );

        List<DisappearNumbersResult> list = List.of(disappearNumbersResult1, disappearNumbersResult2);

        //when
        when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));
        when(disappearNumbersResultRepository.findAllByUserId(any())).thenReturn(list);


        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(DISAPPEAR_NUMBERS_RESULTS_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.results.[0].timeResultInSeconds").value(disappearNumbersResult1.getTimeResultInSeconds()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.results.[1].timeResultInSeconds").value(disappearNumbersResult2.getTimeResultInSeconds()));
    }






}
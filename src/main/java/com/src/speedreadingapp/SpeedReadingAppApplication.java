package com.src.speedreadingapp;

import com.src.speedreadingapp.jpa.appuser.Role;
import com.src.speedreadingapp.jpa.appuser.RoleRepository;
import com.src.speedreadingapp.jpa.understandingmeter.UnderstandingLevelLogService;
import com.src.speedreadingapp.jpa.understandingmeter.textquestions.UnderstandingLevelTextAndQuestionService;
import com.src.speedreadingapp.security.config.Secrets;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Properties;

//exclude={DataSourceAutoConfiguration.class}
@RequiredArgsConstructor
@SpringBootApplication()
public class SpeedReadingAppApplication {

	private final RoleRepository roleRepository;
	private final UnderstandingLevelTextAndQuestionService understandingLevelTextAndQuestionService;

	public static void main(String[] args) {
		SpringApplication.run(SpeedReadingAppApplication.class, args);
	}

	@Bean
	protected void createRoles() {
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(new Role(1L, "ROLE_USER"));
		roles.forEach((r) -> {
			if(roleRepository.findById(r.getId()).isEmpty())
				roleRepository.save(r);
			}
		);

		understandingLevelTextAndQuestionService.createTextAnswerQuestion1();
	}

}
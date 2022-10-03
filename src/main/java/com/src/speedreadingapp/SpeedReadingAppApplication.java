package com.src.speedreadingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

//exclude={DataSourceAutoConfiguration.class}
@SpringBootApplication()
public class SpeedReadingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeedReadingAppApplication.class, args);
	}

}

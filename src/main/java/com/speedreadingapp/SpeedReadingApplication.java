package com.speedreadingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpeedReadingApplication {

    //private final MyConfiguration configuration;

   // public SpeedReadingApplication(MyConfiguration configuration) {
 //       this.configuration = configuration;
  //  }

    public static void main(String[] args) {
        SpringApplication.run(SpeedReadingApplication.class, args);
    }

   /* @Override
    public void run(String... args) {

        Logger logger = LoggerFactory.getLogger(SpeedReadingApplication.class);

        logger.info("----------------------------------------");
        logger.info("Configuration properties");
        logger.info("   username is {}", configuration.getUsername());
        logger.info("   password is {}", configuration.getPassword());
        logger.info("----------------------------------------");
    }*/
}
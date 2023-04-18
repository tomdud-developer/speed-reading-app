
# SpeedReadingApp - backend part

The work aims to design and create a web application that allows you to learn and
improve your speed reading skills. The app offers tools to measure reading speed and text
comprehension. The implemented exercises focus on accelerating perception, expanding
the field of vision, excluding phonetization and regression. The application user has the
option of following a training plan consisting of 21 sessions. The application is divided
into two parts, backend and frontend. The backend of the application was created in Java
using the Spring package. The frontend part of the application uses the React.js library.
Communication between the two components takes place via the REST API. The system
consisting of a database, backend and frontend application was implemented on a home
server using Docker containerization technology.

**Frontend part repository (README is the same): https://github.com/tomdud-developer/speed-reading-app-frontend**

## Tech Stack

**Backend:** Java, Spring, Spring Boot, Tomcat, Spring Security, Gradle, PostgreSQL, Mockito, JUnit, JWT Token

**Frontend:** React.js, Axios, npm, Material UI

**Deployment:** initialy {Heroku, Firebase}, now{Ubuntu Server, Docker, Local Xpose}




## Requirements and futures

**Non-functional requirements:**

    1. Three-tier system architecture
    2. Use of Docker for application containerization
    3. Implementation of a security protocol based on JWT token
    4. User interface color scheme in accordance with guidelines
    5. Automated testability of the backend application
    6. Persistent storage of user data


**Functional requirements:**

    1. Ability to perform exercises that increase perception and reduce regression
    2. Ability to perform exercises that expand the field of view
    3. Ability to measure reading parameters
    4. Ability to track progress on a timeline chart
    5. Ability to change exercise difficulty scale
    6. Ability to import user text used in exercises
    7. Ability to perform exercises according to a training plan
    8. Ability to reset progress in the training plan.

## Architecture
![full_app_structure_diagram.png](assets%2Ffull_app_structure_diagram.png)
![spring_boot_architecture.png](assets%2Fspring_boot_architecture.png)

## Database conception
![database_erd_diagram.png](assets%2Fdatabase_erd_diagram.png)

## Screenshots
![Layout_overview.png](assets%2FLayout_overview.png)
![Fonetization_remover.png](assets%2FFonetization_remover.png)
![History_speedmeterlogs_chart.png](assets%2FHistory_speedmeterlogs_chart.png)

## Deployment

To deploy this project clone repository and run (provide your dockerhub name or use it locally)

**Backend**

```bash
sudo docker build –build-arg JAR_FILE=build/libs/.jar -t tomduddeveloper/speedreadingappbackend:latest .
sudo docker push tomduddeveloper/speedreadingappbackend:latest
docker run <img_name>
```

**Frontend**
```bash
sudo docker build -t tomduddeveloper/speedreadingapp:latest .
sudo docker push tomduddeveloper/speedreadingapp:latest
docker run <img_name>
```

## Tests
The backend application requires both unit and integration testing.
Unit testing ensures the proper functioning of internal system mechanisms, while integration testing ensures the proper operation of mechanisms that connect external systems with the application.
JUnit and Mockito tools were used for testing.
![Tests_Coverage_raport.png](assets%2FTests_Coverage_raport.png)
![Tests_raport.png](assets%2FTests_raport.png)

## Color schema
![theme_colors.png](assets%2Ftheme_colors.png)

![Logo](logo.ico)


## Authors
- [@tomdud-developer](https://www.github.com/tomdud-developer)

## More Screenshots
![Columns_numbers.png](assets%2FmoreScreens%2FColumns_numbers.png)
![Disappering_numbers.png](assets%2FmoreScreens%2FDisappering_numbers.png)
![Fast_words.png](assets%2FmoreScreens%2FFast_words.png)
![Fonetization_remover.png](assets%2FmoreScreens%2FFonetization_remover.png)
![History_speedmeterlogs_chart.png](assets%2FmoreScreens%2FHistory_speedmeterlogs_chart.png)
![Pointer_basic.png](assets%2FmoreScreens%2FPointer_basic.png)
![Pyramid_numbers.png](assets%2FmoreScreens%2FPyramid_numbers.png)
![Reading_speed_meter.png](assets%2FmoreScreens%2FReading_speed_meter.png)
![Schultz_arrays.png](assets%2FmoreScreens%2FSchultz_arrays.png)
![Understand_level_meter.png](assets%2FmoreScreens%2FUnderstand_level_meter.png)






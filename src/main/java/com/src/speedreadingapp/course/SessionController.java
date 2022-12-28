package com.src.speedreadingapp.course;

import com.src.speedreadingapp.jpa.pdfuser.PdfUser;
import com.src.speedreadingapp.registration.RegistrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/session")
public class SessionController {
    private final CourseStructure courseStructure;

    public SessionController(CourseStructure courseStructure) {
        this.courseStructure = courseStructure;
    }

    @GetMapping(value = "/get-whole-structure")
    ArrayList<Session> getStructure() {
        return courseStructure.getCourseStructure();
    }

    @GetMapping(value = "/get-session/{number}")
    Session getSession(@PathVariable Integer number) {
        return courseStructure.getSession(number);
    }

}

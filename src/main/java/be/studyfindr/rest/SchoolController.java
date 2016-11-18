package be.studyfindr.rest;

import be.studyfindr.entities.School;

import java.util.Collections;
import java.util.List;
import be.studyfindr.entities.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchoolController {
    Data dataLayer = new Data();
    @CrossOrigin
    @RequestMapping("/schools")
    public List<School> getAllSchools(@RequestParam(value="name", required=false) String name) {
        if (name == null) {
            return dataLayer.getAllSchools();
        } else {
            return Collections.singletonList(dataLayer.getSchool(name));
        }
    }
}



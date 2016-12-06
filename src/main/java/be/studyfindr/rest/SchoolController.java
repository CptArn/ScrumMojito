package be.studyfindr.rest;

import be.studyfindr.entities.School;

import java.util.Collections;
import java.util.List;
import be.studyfindr.entities.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The SchoolController defines the interface for all School operations.
 * @version 1.0
 */
@RestController
public class SchoolController {
    // connection to database
    private Data dataLayer = new Data();

    /**
     * Returns a list of all schools.
     * @param name Optional query school name
     * @return all found schools
     */
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



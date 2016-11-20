package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    Data dataLayer = new Data();
    @CrossOrigin
    @RequestMapping(path = "/user/{id}/info", method = RequestMethod.GET)
    public User getUserInfo(@PathVariable Long id) {
        if (id != null) {
            try {
                User found = dataLayer.getUser(id);
                return found;
            } catch(Exception e) {
                return null;
            }
        } else {
            return null;
        }

    }
}
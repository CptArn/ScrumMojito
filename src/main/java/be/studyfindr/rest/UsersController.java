package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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

    @RequestMapping(path = "/user/update", method = RequestMethod.POST)
    //public String postUserInfo(@RequestBody User user) {
    public String postUserInfo(@ModelAttribute("updateForm") User user) {
        if (user != null) {
            try {
                dataLayer.updateUser(user);
                return "Update successful";
            } catch(Exception e) {
                return "Update unsuccessful";
            }
        } else {
            return "Update unsuccessful";
        }
    }
}
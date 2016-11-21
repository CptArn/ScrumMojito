package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin
public class UsersController {
    Data dataLayer = new Data();
    @RequestMapping(value = "/user/{id}/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo(@PathVariable("id") long id) {
        User user = dataLayer.getUser(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/{id}/update", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUserInfo(@PathVariable("id") long id, @RequestBody User user) {
        User currentUser = dataLayer.getUser(id);
        if (currentUser == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        } else {
            currentUser.setEmail(user.getEmail());
            currentUser.setFirstname(user.getFirstname());
            currentUser.setLastname(user.getLastname());
            currentUser.setLocation(user.getLocation());
            currentUser.setAge(user.getAge());
            currentUser.setPrefMale(user.getPrefMale());
            currentUser.setPrefFemale(user.getPrefFemale());
            currentUser.setPrefTrans(user.getPrefTrans());
            currentUser.setPrefAge(user.getPrefAge());
            currentUser.setPrefDistance(user.getPrefDistance());
            currentUser.setPrefLocation(user.getPrefLocation());

            dataLayer.updateUser(currentUser);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        }
    }
}
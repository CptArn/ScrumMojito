package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UsersController {
    Data dataLayer = new Data();
    private FacebookLogic fb;

    public UsersController(){
        fb = new FacebookLogic();
    }

    @RequestMapping("user/getmyinfo")
    public ResponseEntity<User> getMyInfo(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) throws IllegalArgumentException {
        be.studyfindr.entities.User s = fb.getMyInfoFromBackend(accessToken, id);
        if(s == null){
            throw new IllegalArgumentException("Invalid login");
        }
        return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo(@PathVariable("id") long id) {
        User user = dataLayer.getUser(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/{id}/update", method = RequestMethod.POST)
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
            currentUser.setPrefAgeMin(user.getPrefAgeMin());
            currentUser.setPrefAgeMax(user.getPrefAgeMax());
            currentUser.setPrefDistance(user.getPrefDistance());
            currentUser.setPrefLocation(user.getPrefLocation());

            dataLayer.updateUser(currentUser);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        }
    }
}
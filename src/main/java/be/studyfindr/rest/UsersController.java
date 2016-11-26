package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        be.studyfindr.entities.User s = fb.getMyInfoFromBackend(accessToken, id);
        if(s == null){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo(@PathVariable("id") long id, @RequestParam("accessToken") String accessToken) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User user;
        try{
            user = dataLayer.getUser(id);
        }catch(Exception ex) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/{id}/update", method = RequestMethod.POST)
    public ResponseEntity<User> updateUserInfo(@PathVariable("id") long id, @RequestParam("accessToken") String accessToken, @RequestBody User user) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User currentUser;
        try{
            currentUser = dataLayer.getUser(id);
            dataLayer.updateUser(user);
        }catch(Exception ex){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);

    }

    @RequestMapping("user/getmyqueue")
    public ResponseEntity<List<User>> getQueue(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<List<User>>(new ArrayList<>(), HttpStatus.OK);
    }
}
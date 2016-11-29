package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.studyfindr.rest.RestResponseEntityExceptionHandler;
import java.util.List;

@RestController
@CrossOrigin
public class UsersController {
    Data dataLayer = new Data();
    private FacebookLogic fb;

    public UsersController(){
        fb = new FacebookLogic();
    }

    @RequestMapping("/user/getmyinfo")
    public ResponseEntity<User> getMyInfo(@RequestParam("accessToken") String accessToken, @RequestParam("id") Long id) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        be.studyfindr.entities.User s;
        try {
            s = fb.getMyInfoFromBackend(accessToken, id);
        }catch(Exception ex) {
            s = null;
        }
        if(s == null){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{target_id}/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo(@PathVariable("target_id") long target_id, @RequestParam("accessToken") String accessToken, @RequestParam("id") long id) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User user;
        try{
            user = dataLayer.getUser(target_id);
        }catch(Exception ex) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/user/{id}/update", method = RequestMethod.POST)
    public ResponseEntity<User> updateUserInfo(@PathVariable("id") long id, @RequestParam("accessToken") String accessToken, @RequestBody User user) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User updated;
        try{
            dataLayer.updateUser(user);
            updated = dataLayer.getUser(id);
        }catch(Exception ex){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(updated, HttpStatus.OK);

    }

    @RequestMapping(path = "/user/{id_to_like}/like", method = RequestMethod.POST)
    public ResponseEntity<User> updateUserInfo(@PathVariable("id_to_like") long id_to_like, @RequestParam("accessToken") String accessToken, @RequestParam("id") long myId) {
        if (!fb.userIsValid(accessToken, myId)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User userToLike;
        try{
            // test id to like
            userToLike = dataLayer.getUser(id_to_like);
            dataLayer.addLike(new Like(myId, id_to_like, true, false));
        }catch(Exception ex){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(userToLike, HttpStatus.OK);
    }

    @RequestMapping("/user/getmyqueue")
    public ResponseEntity<List<User>> getQueue(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
        List<User> users = dataLayer.getQueue(id);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
}
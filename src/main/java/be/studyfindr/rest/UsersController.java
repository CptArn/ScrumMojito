package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The UserController defines the interface for all User operations.
 *
 * @version 1.1
 */
@RestController
@CrossOrigin
public class UsersController {
    // connection to the database
    private Data dataLayer = new Data();

    private FacebookLogic fb;

    /**
     * Inits. the UserController
     */
    public UsersController() {
        fb = new FacebookLogic();
    }

    /**
     * Returns the user object of the requesting user
     *
     * @param accessToken a valid Facebook access token
     * @param id          Facebook user id
     * @return User object
     */
    @RequestMapping("/user/getmyinfo")
    public ResponseEntity<User> getMyInfo(@RequestParam("accessToken") String accessToken, @RequestParam("id") Long id) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        be.studyfindr.entities.User s;
        try {
            s = fb.getMyInfoFromBackend(accessToken, id);
        } catch (Exception ex) {
            s = null;
        }
        if (s == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
    }

    /**
     * Returns the user object of the queried user.
     *
     * @param target_id   id of the queried user
     * @param accessToken a valid access token
     * @param id          Facebook id bound to the access token
     * @return User object
     */
    @RequestMapping(value = "/user/{target_id}/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserInfo(@PathVariable("target_id") long target_id, @RequestParam("accessToken") String accessToken, @RequestParam("id") long id) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        User user;
        try {
            user = dataLayer.getUser(target_id);
        } catch (Exception ex) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * Updates the user information.
     *
     * @param id          id bound to the access token
     * @param accessToken valid access token
     * @param user        User object with new information
     * @return updated user object
     */
    @RequestMapping(path = "/user/{id}/update", method = RequestMethod.POST)
    public ResponseEntity<User> updateUserInfo(@PathVariable("id") long id, @RequestParam("accessToken") String accessToken, @RequestBody User user) {
        // debug
        System.out.println("Req:[POST] /user/" + id + "/update?accessToken=" + accessToken + "\nUser: " + user.toString());
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User updated;
        try {
            dataLayer.updateUser(user);
            updated = dataLayer.getUser(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }

    /**
     * Likes or dislikes a user.
     *
     * @param id_to_like  id to like or dislike
     * @param accessToken valid access token
     * @param myId        own id bound to the access token
     * @param like        true if like, false if dislike
     * @return the liked user object
     */
    @RequestMapping(path = "/user/{id_to_like}/like", method = RequestMethod.POST)
    public ResponseEntity<User> updateLikeUser(@PathVariable("id_to_like") long id_to_like, @RequestParam("accessToken") String accessToken,
                                               @RequestParam("id") long myId, @RequestParam("like") boolean like) {
        if (!fb.userIsValid(accessToken, myId)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User userToLike;
        Like likeMyId = new Like(myId, id_to_like, like);
        try{
            // test id to like
            userToLike = dataLayer.getUser(id_to_like);
            if (userToLike == null || userToLike.getid() == myId)  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            dataLayer.addLike(likeMyId);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userToLike, HttpStatus.OK);
    }

    /**
     * Returns a list of potential candidates
     *
     * @param id          own id bound to the access token
     * @param accessToken valid access token
     * @return list of potential candidates
     */
    @RequestMapping("/user/getmyqueue")
    public ResponseEntity<List<User>> getQueue(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
        List<User> users = dataLayer.getQueue(id);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    /**
     * Returns all of matches for the own user.
     *
     * @param id          own user id
     * @param accessToken valid access token
     * @return list of user objects representing the matches
     */
    @RequestMapping("/user/getmatches")
    public ResponseEntity<List<User>> getMatches(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
        try {
            return new ResponseEntity<List<User>>(dataLayer.getMatches(id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
        }
    }
}
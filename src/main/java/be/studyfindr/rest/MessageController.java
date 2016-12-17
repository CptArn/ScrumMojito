package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
* The UserController defines the interface for all User operations.
* @version 1.0
*/
@RestController
@CrossOrigin
public class MessageController {
    Data dataLayer = new Data();
    private FacebookLogic fb;

    /**
     * Inits. the MessageController
     */
    public MessageController(){
        fb = new FacebookLogic();
    }

    /**
     * Returns a list of all messages between the own user and a match.
     * @param id own id
     * @param accessToken valid access token
     * @param matchId id of the match
     * @return list of messages
     */
    @RequestMapping("/messages/getconversation")
    public ResponseEntity<List<Message>> getConversation(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken, @RequestParam("matchid") long matchId) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            if (!dataLayer.usersHaveMatch(id, matchId)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            List<Message> messages = dataLayer.getMessages(id, matchId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends a message to a match
     * @param id own id
     * @param accessToken valid access token
     * @param matchId id of the matched user
     * @param message message to send
     * @return send message
     */
    @RequestMapping(path = "/messages/postmessage", method = RequestMethod.POST)
    public ResponseEntity<Message> postMessage(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken, @RequestParam("matchid") long matchId, @RequestBody String message) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // message only while match
        if (!dataLayer.usersHaveMatch(id, matchId)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try{
            Message m = new Message(message, new Date(), id, matchId);
            long messId = dataLayer.addMessage(m);
            Message result = dataLayer.getMessage(messId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

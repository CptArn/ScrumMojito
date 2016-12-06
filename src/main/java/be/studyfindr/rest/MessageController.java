package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.util.List;

/*
* The UserController defines the interface for all User operations.
* @version 1.0
*/
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
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<Message>>(HttpStatus.UNAUTHORIZED);
        try{
            return new ResponseEntity<List<Message>>(dataLayer.getMessages(id, matchId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<List<Message>>(HttpStatus.BAD_REQUEST);
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
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<Message>(HttpStatus.UNAUTHORIZED);
        // message only while match
        if (!dataLayer.usersHaveMatch(id, matchId)) return new ResponseEntity<Message>(HttpStatus.UNAUTHORIZED);
        try{
            Message m = new Message(message, new Date(), id, matchId);
            long messId = dataLayer.addMessage(m);
            Message result = dataLayer.getMessage(messId);
            return new ResponseEntity<Message>(result, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }
}

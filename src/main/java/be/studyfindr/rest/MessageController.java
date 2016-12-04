package be.studyfindr.rest;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Message;
import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by anthony on 4/12/2016.
 */
public class MessageController {
    Data dataLayer = new Data();
    private FacebookLogic fb;

    /**
     * Inits. the UserController
     */
    public MessageController(){
        fb = new FacebookLogic();
    }

    @RequestMapping("/messages/getconversation")
    public ResponseEntity<List<Message>> getConversation(@RequestParam("id") long id, @RequestParam("accessToken") String accessToken, @RequestParam("matchid") long matchId) {
        if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<List<Message>>(HttpStatus.UNAUTHORIZED);
        try{
            return new ResponseEntity<List<Message>>(dataLayer.getMessages(id, matchId), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<List<Message>>(HttpStatus.BAD_REQUEST);
        }
    }


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

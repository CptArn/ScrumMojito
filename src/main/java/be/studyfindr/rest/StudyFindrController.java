package be.studyfindr.rest;

import be.studyfindr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by anthony on 20/11/2016.
 */
@RestController
public class StudyFindrController {
    private FacebookLogic fb;

    public StudyFindrController(){
        fb = new FacebookLogic();
    }

    @RequestMapping("/getmyinfo")
    public ResponseEntity<User> getMyInfo(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) throws RestException {
        be.studyfindr.entities.User s = fb.getMyInfoFromBackend(accessToken, id);
        if(s == null){
            throw new RestException("Invalid login");
        }
        return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
    }
}

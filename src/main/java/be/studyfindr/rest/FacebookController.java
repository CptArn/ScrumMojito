package be.studyfindr.rest;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpSession;

import be.studyfindr.entities.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class FacebookController {
 	private FacebookLogic fb;

	public FacebookController() throws IOException {
		fb = new FacebookLogic();
  	}

  	//https://www.facebook.com/dialog/oauth?client_id=1794346987494326&redirect_uri=http://localhost:8080/auth/facebook
  	@RequestMapping("/auth/facebook")
  	public RedirectView facebookAuth(HttpSession session) {
	  	try{
		  	return fb.startAuthentication(session);
	  	}catch(Exception ex){
			return new RedirectView();
	  	}
  	}

  	@RequestMapping("/auth/facebook/callback")
  	public LoginResponse callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
	  	return fb.callBack(code, state, session);
  	}

	@RequestMapping("/facebook/getmyinfo")
	public ResponseEntity<be.studyfindr.entities.User> getMyInfo(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) throws RestException {
		be.studyfindr.entities.User s = fb.getMyInfoFromBackend(accessToken, id);
		if(s == null){
			throw new RestException("Invalid login");
		}
		return new ResponseEntity<be.studyfindr.entities.User>(s, HttpStatus.OK);
	}

	@RequestMapping("/facebook/logout")
	public HashMap<String, String> logout(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) throws RestException {
		if (!fb.userIsValid(accessToken, id)) throw new RestException("Invalid login");
		boolean state = fb.logout(accessToken, id);
		HashMap<String, String> status = new HashMap<String, String>();
		status.put("code", state ? "200" : "500");
		status.put("mesage", state ? "successfully logged out" : "cannot logout current user");
		return status;
	}

	@ExceptionHandler(RestException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}
}
package be.studyfindr.rest;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpSession;

import be.studyfindr.entities.ErrorResponse;
import be.studyfindr.entities.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The FacebookController defines the interface for all Facebook operations.
 * @version 1.1
 */
@RestController
public class FacebookController {
 	// connection to the logic
	private FacebookLogic fb;

	/**
	 * Creates an instance of the FacebookController
	 */
	public FacebookController() {
		fb = new FacebookLogic();
  	}

  	//https://www.facebook.com/dialog/oauth?client_id=1794346987494326&redirect_uri=http://localhost:8080/auth/facebook

	/**
	 * Handles a Facebook login by accepting an authentication token and returning an access token and user ID.
	 * This method expects following usage: http://myhost.com/auth/facebook?code=<Auth. token>
	 * @param session the auth. token
	 * @return will redirect to the Facebook api to request the access token
	 */
	@RequestMapping("/auth/facebook")
  	public RedirectView facebookAuth(HttpSession session) {
	  	try{
		  	return fb.startAuthentication(session);
	  	}catch(Exception ex){
			return new RedirectView();
	  	}
  	}

	/**
	 * Handles the Facebook login callback. This method should not be used directly by a user.
	 * @param code response from Facebook
	 * @param state session state
	 * @param session callback session
	 * @return Login response, will contain the access token and user ID on success.
	 */
  	@RequestMapping("/auth/facebook/callback")
  	public LoginResponse callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
	  	return fb.callBack(code, state, session);
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
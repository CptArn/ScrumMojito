package be.studyfindr.rest;

import java.util.HashMap;
import javax.servlet.http.HttpSession;

import be.studyfindr.entities.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The FacebookController defines the interface for all Facebook operations.
 * @version 1.1
 */
@RestController
@CrossOrigin
public class FacebookController {
 	// connection to the logic
	private FacebookLogic fb;

	/**
	 * Creates an instance of the FacebookController
	 */
	public FacebookController() {
		fb = new FacebookLogic();
  	}

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
			throw new IllegalArgumentException("Something went wrong @/auth/facebook.");
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
  	public ResponseEntity<LoginResponse> callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
	  	try{
			return new ResponseEntity<LoginResponse>(fb.callBack(code, state, session), HttpStatus.OK);
		}catch(Exception ex){
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}

  	}

	/**
	 * Handles a Facebook logout by invalidating the access token.
	 * @param accessToken access token to invalidate.
	 * @param id id associated with the token.
	 * @return status code and message.
	 */
	@RequestMapping(path = "/facebook/logout", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String, String>> logout(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) {
		if (!fb.userIsValid(accessToken, id)) return new ResponseEntity<HashMap<String, String>>(HttpStatus.UNAUTHORIZED);
		boolean state = fb.logout(accessToken, id);
		if (!state) return new ResponseEntity<HashMap<String, String>>(HttpStatus.BAD_REQUEST);
		HashMap<String, String> status = new HashMap<String, String>();
		status.put("mesage", "successfully logged out");
		return new ResponseEntity<HashMap<String, String>>(status, HttpStatus.OK);
	}

	/**
	 * An alternative login method based on an access token.
	 * @param accessToken valid access token for the user
	 * @param id id of the user
	 * @return returns a login response with access token and user id
	 */
	@RequestMapping(path = "/facebook/login", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> login(@RequestParam("accessToken") String accessToken, @RequestParam("id") long id) {
		// TODO check user access token - id combo without using fb.isUserValid()
		User me = fb.getMyInfoFromFacebook(accessToken);
		if (!fb.newUserHandler(me)) return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<LoginResponse>(new LoginResponse(accessToken, id), HttpStatus.OK);
	}
}
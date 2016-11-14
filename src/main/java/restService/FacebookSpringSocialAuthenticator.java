package restService;

import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FacebookSpringSocialAuthenticator {
  public static final String STATE = "state";
  private String applicationHost;
  private FacebookConnectionFactory facebookConnectionFactory;
 
  public FacebookSpringSocialAuthenticator() {
    this.applicationHost = "https://studyfindr.herokuapp.com";
    facebookConnectionFactory =
      new FacebookConnectionFactory("1794346987494326", "70d05b6625be77ee4db63a8d529e7746");
  }
  
  //https://www.facebook.com/dialog/oauth?client_id=1794346987494326&redirect_uri=http://localhost:8080/auth/facebook
  @RequestMapping("/auth/facebook")
  public RedirectView startAuthentication(HttpSession session)
      throws Exception {
    String state = UUID.randomUUID().toString();
    session.setAttribute(STATE, state);
   
    OAuth2Operations oauthOperations =
        facebookConnectionFactory.getOAuthOperations();
    OAuth2Parameters params = new OAuth2Parameters();
    params.setRedirectUri(applicationHost + "/auth/facebook/callback");
    params.setState(state);
   
    String authorizeUrl = oauthOperations.buildAuthorizeUrl(
        GrantType.AUTHORIZATION_CODE, params);
    return new RedirectView(authorizeUrl);
  }
  
  @RequestMapping("/auth/facebook/callback")
  @ResponseBody
  public String callBack(@RequestParam("code") String code,
                               @RequestParam("state") String state,
                               HttpSession session) {
    String stateFromSession = (String) session.getAttribute(STATE);
    session.removeAttribute(STATE);
    if (!state.equals(stateFromSession)) {
    	return "OOPS";
    }
   
    AccessGrant accessGrant = getAccessGrant(code);
   
    String facebookUserId = getFacebookUserId(accessGrant);
    session.setAttribute("facebookUserId", facebookUserId);
    //return new RedirectView("/logged-in");
    //return facebookUserId;
    return getFacebookUserName(accessGrant);
  }
  
  private AccessGrant getAccessGrant(String authorizationCode) {
	  OAuth2Operations oauthOperations =
	      facebookConnectionFactory.getOAuthOperations();
	  return oauthOperations.exchangeForAccess(authorizationCode,
	      applicationHost + "/auth/facebook/callback", null);
	}
  
  private String getFacebookUserId(AccessGrant accessGrant) {
	  Connection<Facebook> connection =
	      facebookConnectionFactory.createConnection(accessGrant);
	  ConnectionKey connectionKey = connection.getKey();
	  return connectionKey.getProviderUserId();
	}
  
  private String getFacebookUserName(AccessGrant accessGrant) {
	  Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
	  Facebook facebook = connection.getApi();
	  String [] fields = { "id", "email",  "first_name", "last_name" };
	  User userProfile = facebook.fetchObject("me", User.class, fields);
	  return userProfile.getFirstName();
	}
}
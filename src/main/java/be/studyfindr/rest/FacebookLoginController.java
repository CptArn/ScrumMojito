package be.studyfindr.rest;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class FacebookLoginController {
  	public static final String STATE = "state";
  	private String applicationHost;
  	private FacebookConnectionFactory facebookConnectionFactory;
 
  public FacebookLoginController() throws IOException {
	  Resource resource = new ClassPathResource("application.properties");
	  Properties props = PropertiesLoaderUtils.loadProperties(resource);
	  this.applicationHost = props.getProperty("studyfindr.host");
	  facebookConnectionFactory =
			  new FacebookConnectionFactory(props.getProperty("spring.social.facebook.appId"), props.getProperty("spring.social.facebook.appSecret"));
  }

  //https://www.facebook.com/dialog/oauth?client_id=1794346987494326&redirect_uri=http://localhost:8080/auth/facebook
  @RequestMapping("/auth/facebook")
  public RedirectView startAuthentication(HttpSession session) throws Exception {
	  String state = UUID.randomUUID().toString();
	  session.setAttribute(STATE, state);
	  OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
	  OAuth2Parameters params = new OAuth2Parameters();
	  params.setRedirectUri(applicationHost + "/auth/facebook/callback");
	  params.setState(state);

	  String authorizeUrl = oauthOperations.buildAuthorizeUrl(
	  		GrantType.AUTHORIZATION_CODE, params);
	  return new RedirectView(authorizeUrl);
  }

  @RequestMapping("/auth/facebook/callback")
  public AccessGrant callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
	  String stateFromSession = (String) session.getAttribute(STATE);
	  session.removeAttribute(STATE);
	  if (!state.equals(stateFromSession)) {
		  return null;
	  }

	  AccessGrant accessGrant = getAccessGrant(code);
	  String facebookUserId = getFacebookUserId(accessGrant);
	  session.setAttribute("facebookUserId", facebookUserId);
	  return accessGrant;
  }

  private AccessGrant getAccessGrant(String authorizationCode) {
	  OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
	  return oauthOperations.exchangeForAccess(authorizationCode, applicationHost + "/auth/facebook/callback", null);
  }

  private String getFacebookUserId(AccessGrant accessGrant) {
	  Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
	  ConnectionKey connectionKey = connection.getKey();
	  return connectionKey.getProviderUserId();
  }
}
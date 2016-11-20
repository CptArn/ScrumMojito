package be.studyfindr.rest;
import java.util.Properties;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import be.studyfindr.entities.Data;
import be.studyfindr.entities.LoginResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

public class FacebookLogic {

    public static final String STATE = "state";
    public final String[] fields = { "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift", "website", "work"};

    public String applicationHost;
    public FacebookConnectionFactory facebookConnectionFactory;
    Data backend;

    public FacebookLogic(){
        Resource resource = new ClassPathResource("application.properties");
        backend = new Data();
        try{
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            this.applicationHost = props.getProperty("studyfindr.host");
            facebookConnectionFactory =
                    new FacebookConnectionFactory(props.getProperty("spring.social.facebook.appId"), props.getProperty("spring.social.facebook.appSecret"));
        }catch(Exception ex){
            // defaults
            this.applicationHost = "http://localhost:8080";
            facebookConnectionFactory =
                    new FacebookConnectionFactory("1794346987494326", "70d05b6625be77ee4db63a8d529e7746");
        }
    }

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

    public LoginResponse callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
        String stateFromSession = (String) session.getAttribute(STATE);
        session.removeAttribute(STATE);
        if (!state.equals(stateFromSession)) {
            return null;
        }

        AccessGrant accessGrant = getAccessGrant(code);
        String facebookUserId = getFacebookUserId(accessGrant);
        session.setAttribute("facebookUserId", facebookUserId);

        newUserHandler(getMyInfoFromFacebook(accessGrant.getAccessToken()));

        return new LoginResponse(accessGrant.getAccessToken(), Long.parseLong(facebookUserId));
    }

    private boolean newUserHandler(User user){
        try{
            backend.getUser(Long.parseLong(user.getId()));
            // TODO update user data in DB if data on FB changes.
            return true;
        }catch(NullPointerException ex){
            // create a new user
            be.studyfindr.entities.User newUser = new be.studyfindr.entities.User(
                    Long.parseLong(user.getId()),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    null,
                    user.getAgeRange().getMin()
            );
            backend.addUser(newUser);
            return true;
        }catch(Exception ex){
            return false;
        }
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

    public User getMyInfoFromFacebook(String accessToken){
        Facebook facebook;
        try {
            Connection<Facebook> connection = facebookConnectionFactory.createConnection(new AccessGrant(accessToken));
            facebook = connection.getApi();
        }catch(Exception ex) {
            return null;
        }
        return facebook.fetchObject("me", User.class, fields);
    }

    public final boolean userIsValid(String accessToken, long id){
        User user = getMyInfoFromFacebook(accessToken);
        if (user == null) return false;
        return Long.parseLong(user.getId()) == id;
    }

    public be.studyfindr.entities.User getMyInfoFromBackend(String accessToken, long id){
        if (!userIsValid(accessToken, id)) return null;
        return backend.getUser(id);
    }

    public boolean logout(String accessToken, long id) {
        try{
            Connection<Facebook> connection = facebookConnectionFactory.createConnection(new AccessGrant(accessToken));
            connection.getApi().delete("me/permissions");
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}

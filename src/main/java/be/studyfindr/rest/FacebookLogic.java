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

/**
 * FacebookLogic contains all logic operations to interact with Facebook.
 * @version 1.1
 */
public class FacebookLogic {

    // Default state for a session.
    public static final String STATE = "state";

    // Fields to fetch from the Facebook API.
    public final String[] fields = {
            "id", "about", "age_range", "birthday",
            "context", "cover", "currency", "devices",
            "education", "email", "favorite_athletes",
            "favorite_teams", "first_name", "gender",
            "hometown", "inspirational_people", "installed",
            "install_type", "is_verified", "languages", "last_name",
            "link", "locale", "location", "meeting_for",
            "middle_name", "name", "name_format",
            "political", "quotes", "payment_pricepoints",
            "relationship_status", "religion", "security_settings",
            "significant_other", "sports", "test_group", "timezone",
            "third_party_id", "updated_time", "verified", "video_upload_limits",
            "viewer_can_send_gift", "website", "work"};

    // Placeholder for the current host.
    public String applicationHost;

    // Placeholder for the active connections.
    public FacebookConnectionFactory facebookConnectionFactory;

    // Connection to the backend.
    Data backend;

    /**
     * Creates an instance of the Facebook logic.
     * All settings should be defined in 'application.properties'.
     * If this file is not available defaults will be used:
     * - Host: http://localhost:8080
     * - App-id: 1794346987494326
     * - App-secret: 70d05b6625be77ee4db63a8d529e7746
     */
    public FacebookLogic(){
        Resource resource = new ClassPathResource("application.properties");
        backend = new Data();
        try{
            // read application.properties
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            this.applicationHost = props.getProperty("studyfindr.host");
            facebookConnectionFactory =
                    new FacebookConnectionFactory(props.getProperty("spring.social.facebook.appId"), props.getProperty("spring.social.facebook.appSecret"));
        }catch(Exception ex){
            // fallback to defaults
            this.applicationHost = "http://localhost:8080";
            facebookConnectionFactory =
                    new FacebookConnectionFactory("1794346987494326", "70d05b6625be77ee4db63a8d529e7746");
        }
    }

    /**
     * Starts the authentication process with Facebook by exchanging  the auth. token with an access token.
     * @param session user request containing the auth. token.
     * @return redirect request to Facebook.
     * @throws Exception if the process fails.
     */
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

    /**
     * Handles the Facebook callback and returns a valid access token on success.
     * @param code access token from Facebook.
     * @param state session state.
     * @param session response from Facebook.
     * @return LoginResponse containing an access token and user ID.
     */
    public LoginResponse callBack(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
        String stateFromSession = (String) session.getAttribute(STATE);

        // Check state in Facebook response
        session.removeAttribute(STATE);
        if (!state.equals(stateFromSession)) {
            return null;
        }

        AccessGrant accessGrant = getAccessGrant(code);
        String facebookUserId = getFacebookUserId(accessGrant);

        // try to add user to database, return null on failure.
        if (!newUserHandler(getMyInfoFromFacebook(accessGrant.getAccessToken()))) return null;

        return new LoginResponse(accessGrant.getAccessToken(), Long.parseLong(facebookUserId));
    }

    /**
     * Tries to add the user to the database, if the user already exists no action will be taken.
     * @param user user to add to the database.
     * @return true on success, false if something went wrong.
     */
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
                    user.getAgeRange().getMin(),
                    false,
                    false,
                    false,
                    18,
                    25,
                    1
            );
            backend.addUser(newUser);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    /**
     * Helper method for auth. to access token exchange.
     * @param authorizationCode auth. token.
     * @return AccessGrant
     */
    private AccessGrant getAccessGrant(String authorizationCode) {
        OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
        return oauthOperations.exchangeForAccess(authorizationCode, applicationHost + "/auth/facebook/callback", null);
    }

    /**
     * Gets the user ID from Facebook based on the access token.
     * @param accessGrant a valid access token.
     * @return associated user ID.
     */
    private String getFacebookUserId(AccessGrant accessGrant) {
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        ConnectionKey connectionKey = connection.getKey();
        return connectionKey.getProviderUserId();
    }

    /**
     * Gets the profile information from Facebook.
     * This method replaces the Spring method Facebook::userOperations().getUserProfile()
     * @param accessToken valid access token.
     * @return User object containing all profile details.
     */
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

    /**
     * Checks if the login session is valid by requesting the user ID from Facebook based on the given access token.
     * This ID is compared to the user ID from the input and these should match to be valid.
     * @param accessToken valid access token
     * @param id id to check
     * @return true if user is valid.
     */
    public final boolean userIsValid(String accessToken, long id){
        User user = getMyInfoFromFacebook(accessToken);
        if (user == null) return false;
        return Long.parseLong(user.getId()) == id;
    }

    /**
     * Gets a user profile from the database based on id.
     * @param accessToken valid access token.
     * @param id id to find.
     * @return user profile, null on failure.
     */
    public be.studyfindr.entities.User getMyInfoFromBackend(String accessToken, long id){
        if (!userIsValid(accessToken, id)) return null;
        return backend.getUser(id);
    }

    /**
     * Logs out the current user by invalidating the access token.
     * @param accessToken valid access token.
     * @param id user id to logout.
     * @return true on success, false on failure.
     */
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

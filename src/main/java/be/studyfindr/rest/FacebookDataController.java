package be.studyfindr.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by anthony on 19/11/2016.
 */
@RestController
public class FacebookDataController {
    private FacebookConnectionFactory facebookConnectionFactory;
    public final String[] fields = { "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift", "website", "work"};

    public FacebookDataController() throws IOException {
        Resource resource = new ClassPathResource("application.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        facebookConnectionFactory =
                new FacebookConnectionFactory(props.getProperty("spring.social.facebook.appId"), props.getProperty("spring.social.facebook.appSecret"));
    }

    @RequestMapping("/facebook/getMyInfo")
    private User getMyInfo(@RequestParam("accessToken") String accessToken) {
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(new AccessGrant(accessToken));
        Facebook facebook = connection.getApi();
        User userProfile = facebook.fetchObject("me", User.class, fields);
        return userProfile;
    }
}

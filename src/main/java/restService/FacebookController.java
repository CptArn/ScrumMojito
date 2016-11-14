package restService;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/login")
@Controller
public class FacebookController {

	
	private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public FacebookController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping("/facebook_oauth")
    @ResponseBody
    public User helloFacebook(@RequestParam(value="code") String code) {
    	facebook = new FacebookTemplate(code);
    	return facebook.userOperations().getUserProfile();
    	//return "oAUTH code: " + code;
    }
	
}
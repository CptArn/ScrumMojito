package restService;

import javax.servlet.http.HttpServletRequest;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/login")
@Controller
public class FacebookController{
	
	@RequestMapping("/fb")
    @ResponseBody
    public String fb(@RequestParam(value="code") String code) {
		return SpringSocial.getInstance().getID("facebook", code);
    	//return facebook.userOperations().getUserProfile();
    	//return "oAUTH code: " + request.getParameter("code");
		// https://www.facebook.com/dialog/oauth?client_id=1794346987494326&redirect_uri=http://localhost:8080/login/fb
    }
	
}
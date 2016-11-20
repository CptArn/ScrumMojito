package be.studyfindr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import be.studyfindr.rest.FacebookLoginController;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FacebookControllerTest {
    private MockMvc mockMvc;

    private FacebookLoginController controller;

    @Before
    public void setUp() {
        try {
            controller = new FacebookLoginController();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1FoundandRedirected() throws Exception {
        this.mockMvc.perform(get("/auth/facebook"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("https://www.facebook.com/**/*"));
    }
}








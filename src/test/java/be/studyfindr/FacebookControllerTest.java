package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.LoginResponse;
import be.studyfindr.entities.User;
import be.studyfindr.rest.FacebookController;
import be.studyfindr.rest.FacebookLogic;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.InvocationTargetException;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FacebookControllerTest {
    private MockMvc mockMvc;

    private FacebookController controller;

    static User u1;
    static Data dataLayer;

    @Before
    public void setUp() {
        try {
            controller = new FacebookController();
            u1 = new User(1, "email@email.com", "Jan", "Peeters", 18, true, false, true, 18, 35, 25, 1, false, false, 0.0, 0.0, "gent");
            dataLayer = new Data();
            dataLayer.addUser(u1);
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

    @Test
    public void test2LoginResponse(){
        LoginResponse l = new LoginResponse("testtoken", 123);
        assert(l.getAccessToken().equals("testtoken") && (l.getId() == 123));
    }

    @Test
    public void test3LoginResponse(){
        LoginResponse l = new LoginResponse();
        l.setAccessToken("testtoken");
        l.setId(123);
        assert(l.getAccessToken().equals("testtoken") && (l.getId() == 123));
    }

    @Test
    public void test3Callback() throws Exception {
        try {
            mockMvc.perform(get("/auth/facebook/callback")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("code", "abc123")
                    .param("state", FacebookLogic.STATE)
                    .content("null")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void test4GetAccessGrantTest(){
        FacebookLogic fb = new FacebookLogic();
        AccessGrant a = fb.getAccessGrant("");
        fail("expected Bad Request");
    }

    @Test
    public void test5LogoutInvalidUser(){
        try {
            mockMvc.perform(post("/facebook/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "")
                    .param("id", "1")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnauthorized());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test6LogoutUnknownUser(){
        try {
            mockMvc.perform(post("/facebook/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteUser(u1);
    }
}








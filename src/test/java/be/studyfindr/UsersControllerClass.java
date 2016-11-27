package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.User;
import be.studyfindr.rest.UsersController;
import org.apache.tomcat.util.digester.ArrayStack;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersControllerClass {
    static User u1;
    static User u2;
    static User u3;
    static Like l1;
    static Like l2;
    static Data dataLayer;
    static MockMvc mockMvc;

    UsersController controller;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeClass
    public static void setUp() {
        u1 = new User(1, "email@email.com", "Jan", "Peeters", "Oiljst", 18, true, false, true, 18, 35, 25, 1, false, false);
        u2 = new User(2, "email2@email.com", "Silke", "Yolo", "Gentj", 22, false, true, false, 18, 35, 25, 1, false, false);
        u3 = new User(3, "email3@email.com", "sander", "maes", "Gentj", 22, false, true, false, 18, 35, 25, 1, false, false);
        l1 = new Like(2, 3, true, false);
        l2 = new Like(1, 3, true, false);
        dataLayer = new Data();
        dataLayer.addUser(u1);
        dataLayer.addUser(u2);
        dataLayer.addUser(u3);
        dataLayer.addLike(l1);
        dataLayer.addLike(l2);
    }

    @Before
    public void setUpMockMVC() {
        try {
            controller = new UsersController();
            mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1GetInfo() {
        try {
            MvcResult result = this.mockMvc.perform(get("/user/1/info?accessToken=testtoken")).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test2GetInfo() {
        try {
            this.mockMvc.perform(get("/user/-1/info?accessToken=testtoken")).andExpect(status().is4xxClientError());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test3GetMyInfoWithSuccess() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=testtoken&id=" + u1.getid())
            ).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test4GetMyInfoFail() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=INVALID&id=" + u1.getid())
            ).andExpect(status().is4xxClientError()).andReturn();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test5GetMyInfoFail() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=testtoken&id=-1")
            ).andExpect(status().is4xxClientError()).andReturn();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test6UpdateSuccess() {
        try {
            User newUser = u1.clone();
            newUser.setFirstname("newName");
            mockMvc.perform(post("/user/1/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .content(this.json(newUser))
                    .accept(MediaType.APPLICATION_JSON)

            ).andExpect(status().is2xxSuccessful());
            User fromBackend = dataLayer.getUser(1);
            assert(fromBackend.getFirstname().equals("newName"));

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test7UpdateNotFound() {
        try {
            User newUser = u1.clone();
            newUser.setFirstname("newName");
            mockMvc.perform(post("/user/-1/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .content(this.json(newUser))
                    .accept(MediaType.APPLICATION_JSON)

            ).andExpect(status().isNotFound());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test8LikeNotFound() {
        try {
            mockMvc.perform(post("/user/-1/like")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test9LikeSuccess() {
        try {

            mockMvc.perform(post("/user/2/like")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            Like like = dataLayer.getLike(u1.getid(), u2.getid());
            assert((like.getLikee_Id() + like.getLiker_Id()) == (u1.getid() + u2.getid()));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test10getmyqueue() {
        String resString;
        try {
            MvcResult result = mockMvc.perform(get("/user/getmyqueue")
                    .param("accessToken", "testtoken")
                    .param("id", u3.getid() + "")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(status().isOk()).andReturn();
            resString = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(resString);
            List<User> users = new ArrayList<User>();
            for (int i = 0; i < jsonarray.length(); i++) {
                users.add(new User(Document.parse(jsonarray.get(i).toString())));
            }
            assert(users.contains(u1) && users.contains(u2));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteUser(u1);
        dataLayer.deleteUser(u2);
        dataLayer.deleteUser(u3);
        dataLayer.deleteLike(new Like(u1.getid(), u2.getid(), true, false));
        dataLayer.deleteLike(l1);
        dataLayer.deleteLike(l2);
    }
}

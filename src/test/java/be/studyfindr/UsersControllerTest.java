package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.User;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
import org.json.JSONArray;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersControllerTest {
    static User u1;
    static User u2;
    static User u3;
    static User uu1;
    static User uu2;
    static User uu3;
    static Like l1;
    static Like l2;
    static Like ll1;
    static Like ll2;
    static Like ll3;
    static Data dataLayer;
    static MockMvc mockMvc;

    UsersController controller;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeClass
    public static void setUp() {
        u1 = new User(1, "email@email.com", "Jan", "Peeters", 18, true, false, true, 18, 35, 25, 1, false, false, 0.1, 0.2, "gent");
        u2 = new User(2, "email2@email.com", "Silke", "Yolo", 22, false, true, false, 18, 35, 25, 1, false, false, 0.1, 0.2, "gent");
        u3 = new User(3, "email3@email.com", "sander", "maes", 22, false, true, false, 18, 35, 25, 1, false, false, 0.1, 0.2, "gent");
        l1 = new Like(2, 3, true);
        l2 = new Like(1, 3, true);
        uu1 = new User(50, "email1@email.com", "Jan", "Peeters", 18, false, true, false, 16, 35, 6, 1, true, false, 50.902440, 4.005659, "haaltert");
        uu2 = new User(51, "email2@email.com", "Nele", "Mertens", 18, true, false, false, 16, 35, 20, 1, false, true, 50.937810, 4.040952, "aalst");
        uu3 = new User(52, "email3@email.com", "Bart", "Jansens", 18, false, true, true, 18, 35, 25, 1, false, false, 50.965860, 3.977374, "lede");
        ll1 = new Like(uu1.getid(), uu2.getid(), true);
        ll2 = new Like(uu2.getid(), uu1.getid(), true);
        ll3 = new Like(uu1.getid(), uu3.getid(), true);
        dataLayer = new Data();
        dataLayer.addUser(u1);
        dataLayer.addUser(u2);
        dataLayer.addUser(u3);
        dataLayer.addLike(l1);
        dataLayer.addLike(l2);
        dataLayer.addUser(uu1);
        dataLayer.addUser(uu2);
        dataLayer.addUser(uu3);
        dataLayer.addLike(ll1);
        dataLayer.addLike(ll2);
        dataLayer.addLike(ll3);
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
    public void test01GetInfo() {
        try {
            MvcResult result = this.mockMvc.perform(get("/user/1/info?accessToken=testtoken&id=1")).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test02GetInfo() {
        try {
            this.mockMvc.perform(get("/user/-1/info?accessToken=testtoken")).andExpect(status().is4xxClientError());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test03GetMyInfoWithSuccess() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=testtoken&id=" + u1.getid())
            ).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test04GetMyInfoFail() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=INVALID&id=" + u1.getid())
            ).andExpect(status().is4xxClientError()).andReturn();
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test05GetMyInfoFail() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=testtoken&id=-1")
            ).andExpect(status().is4xxClientError()).andReturn();
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test06UpdateSuccess() {
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
            fail(e.getMessage());
        }
    }

    @Test
    public void test07UpdateNotFound() {
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
            fail(e.getMessage());
        }
    }

    @Test
    public void test08LikeNotFound() {
        try {
            mockMvc.perform(post("/user/-1/like")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .param("like", "true")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test09LikeSuccess() {
        try {
            mockMvc.perform(post("/user/2/like")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .param("like", "true")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            Like like = dataLayer.getLike(u1.getid(), u2.getid());
            assert((like.getLikee_Id() + like.getLiker_Id()) == (u1.getid() + u2.getid()));
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test10getmyqueue() {
        Like rem = dataLayer.getLike(uu1.getid(), uu2.getid());
        if (rem != null) dataLayer.deleteLike(rem);
        String resString;
        try {
            MvcResult result = mockMvc.perform(get("/user/getmyqueue")
                    .param("accessToken", "testtoken")
                    .param("id", uu1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(status().isOk()).andReturn();
            resString = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(resString);
            List<User> users = new ArrayList<User>();
            for (int i = 0; i < jsonarray.length(); i++) {
                users.add(new User(Document.parse(jsonarray.get(i).toString())));
            }
            assert(users.contains(uu2) && !users.contains(uu3));
        } catch(Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void test11Dislike() {
        Like like = new Like(uu1.getid(), uu2.getid(), false);
        dataLayer.addLike(like);
        String resString;
        try {
            MvcResult result = mockMvc.perform(get("/user/getmyqueue")
                    .param("accessToken", "testtoken")
                    .param("id", uu1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(status().isOk()).andReturn();
            resString = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(resString);
            List<User> users = new ArrayList<User>();
            for (int i = 0; i < jsonarray.length(); i++) {
                users.add(new User(Document.parse(jsonarray.get(i).toString())));
            }
            assert(!users.contains(uu2) && !users.contains(uu3));
        } catch(Exception e) {
            fail(e.getMessage());
        }

        dataLayer.deleteLike(like);
    }

    @Test
    public void test12GetMatches() {
        dataLayer.addLike(ll1);
        dataLayer.addLike(ll2);
        dataLayer.addLike(ll3);
        try {
            MvcResult result = mockMvc.perform(post("/user/getmatches")
                    .param("accessToken", "testtoken")
                    .param("id", uu1.getid() + "")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(status().isOk()).andReturn();
            String res = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(res);
            List<User> users = new ArrayList<>();
            for (int i = 0; i < jsonarray.length(); i++) {
                users.add(new User(Document.parse(jsonarray.get(i).toString())));
            }
            assert(users.contains(uu2) && !users.contains(uu3));
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test13GetUserInfoNotFound(){
        try {
            this.mockMvc.perform(get("/user/-1/info?accessToken=testtoken&id=1")).andExpect(status().is4xxClientError());
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test14LikeSuccessWithUpdate() {
        try {
            mockMvc.perform(post("/user/2/like")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("accessToken", "testtoken")
                    .param("id", u1.getid() + "")
                    .param("like", "false")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            Like like = dataLayer.getLike(u1.getid(), u2.getid());
            assert((like.getLikee_Id() + like.getLiker_Id()) == (u1.getid() + u2.getid()));
        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test15getmyqueueInvalidUserWithTestToken() {
        try{
            mockMvc.perform(get("/user/getmyqueue")
                    .param("accessToken", "testtoken")
                    .param("id", "-1")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(status().isBadRequest());
        }catch(Exception ex){
            fail(ex.getMessage());
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
        Like remove = dataLayer.getLike(u1.getid(), u2.getid());
        if (remove != null) dataLayer.deleteLike(remove);
        dataLayer.deleteLike(l1);
        dataLayer.deleteLike(l2);
        dataLayer.deleteUser(uu1);
        dataLayer.deleteUser(uu2);
        dataLayer.deleteUser(uu3);
        dataLayer.deleteLike(ll1);
        dataLayer.deleteLike(ll2);
        dataLayer.deleteLike(ll3);
    }
}

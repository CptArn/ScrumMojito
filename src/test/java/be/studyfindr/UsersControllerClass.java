package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import be.studyfindr.rest.FacebookController;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersControllerClass {
    static User u1;
    static Data dataLayer;
    static MockMvc mockMvc;

    UsersController controller;

    @BeforeClass
    public static void setUp() {
        u1 = new User(1, "email@email.com", "Jan", "Peeters", "Oiljst", 18, true, false, true, 18, 35, 25, 1, false, false);
        dataLayer = new Data();
        dataLayer.addUser(u1);
    }

    @Before
    public void setUpMockMVC() {
        try {
            controller = new UsersController();
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
    public void test2GetMyInfoWithSuccess() {
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
    public void test3GetMyInfoFail() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/user/getmyinfo?accessToken=INVALID&id=" + u1.getid())
            ).andExpect(status().is4xxClientError()).andReturn();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }



    /*@Test
    public void test1PutInfo() {
        try {
            String json = u1.toString();
            MvcResult result = this.mockMvc.perform(put("/user/1/update")
                    .contentType(MediaType.APPLICATION_JSON).content(json)
            ).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }*/

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteUser(u1);
    }
}

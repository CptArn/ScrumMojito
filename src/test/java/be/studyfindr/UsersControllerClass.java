package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
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
import java.util.Arrays;

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
        dataLayer = new Data();
        dataLayer.addUser(u1);
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

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteUser(u1);
    }
}

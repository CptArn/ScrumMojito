package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.Like;
import be.studyfindr.entities.Message;
import be.studyfindr.entities.User;
import be.studyfindr.rest.MessageController;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
import org.json.JSONArray;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by anthony on 4/12/2016.
 */
public class MessageControllerTest {
    static User u1;
    static User u2;
    static User u3;
    static Like l1;
    static Like l2;
    static Data dataLayer;
    static MockMvc mockMvc;

    MessageController controller;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeClass
    public static void setUp() {
        u1 = new User(1, "email@email.com", "Jan", "Peeters", 18, true, false, true, 18, 35, 25, 1, false, false, 0.0, 0.0, "gent");
        u2 = new User(2, "email2@email.com", "Silke", "Yolo", 22, false, true, false, 18, 35, 25, 1, false, false, 0.0, 0.0, "gent");
        u3 = new User(3, "email3@email.com", "sander", "maes", 22, false, true, false, 18, 35, 25, 1, false, false, 0.0, 0.0, "gent");
        l1 = new Like(u1.getid(), u2.getid(), true, false);
        l2 = new Like(u2.getid(), u1.getid(), true, false);
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
            controller = new MessageController();
            mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test01CreateChatSessionFail() {
        try {
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u3.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u1.getid() + "")
                    .content(this.json("TEST"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnauthorized());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test02CreateChatSession() throws Exception {
        try {
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u1.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u2.getid() + "")
                    .content(this.json("TEST1"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u2.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u1.getid() + "")
                    .content(this.json("TEST2"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u1.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u2.getid() + "")
                    .content(this.json("TEST3"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            MvcResult result = this.mockMvc.perform(
                    get("/messages/getconversation?id=" + u1.getid() + "&accessToken=testtoken&matchid=" + u2.getid())
            ).andExpect(status().isOk()).andReturn();
            String resString = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(resString);
            List<Message> messages = new ArrayList<Message>();
            for (int i = 0; i < jsonarray.length(); i++) {
                messages.add(new Message(Document.parse(jsonarray.get(i).toString())));
            }
            dataLayer.deleteConversation(u1.getid(), u2.getid());
            assert(messages.size() > 2);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }



    /*@Test
    public void test03RemoveMessages() {
        try {
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u1.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u2.getid() + "")
                    .content(this.json("TEST1"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u2.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u1.getid() + "")
                    .content(this.json("TEST2"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            mockMvc.perform(post("/messages/postmessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id", u1.getid() + "")
                    .param("accessToken", "testtoken")
                    .param("matchid", u2.getid() + "")
                    .content(this.json("TEST3"))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
            List<User> temp = dataLayer.getMatches(u1.getid());
            l1.setLike(false);
            dataLayer.updateLike(l1);
            MvcResult result = this.mockMvc.perform(
                    get("/messages/getconversation?id=" + u1.getid() + "&accessToken=testtoken&matchid=" + u2.getid())
            ).andExpect(status().isOk()).andReturn();
            String resString = result.getResponse().getContentAsString();
            JSONArray jsonarray = new JSONArray(resString);
            assert(jsonarray.length() < 1);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }*/

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
        dataLayer.deleteLike(l1);
        dataLayer.deleteLike(l2);
    }
}

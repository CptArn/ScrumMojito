package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.User;
import be.studyfindr.rest.FacebookController;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        u1 = new User(1, "email@email.com", "Jan", "Peeters", "Oiljst", 18, true, false, true);
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
            MvcResult result = this.mockMvc.perform(get("/user/1/info")).andExpect(status().isOk()).andReturn();
            User user = new User(Document.parse(result.getResponse().getContentAsString()));
            assert(user.equals(u1));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteUser(u1);
    }
}

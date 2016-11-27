package be.studyfindr;

import be.studyfindr.entities.Data;
import be.studyfindr.entities.School;
import be.studyfindr.entities.User;
import be.studyfindr.rest.SchoolController;
import be.studyfindr.rest.UsersController;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by anthony on 27/11/2016.
 */
public class SchoolControllerTest {

    static Data dataLayer;
    static MockMvc mockMvc;
    private static School s1;

    private SchoolController controller;

    @BeforeClass
    public static void setUp() {
        dataLayer = new Data();
        s1 = new School("test", "test");
    }

    @Before
    public void setUpMockMVC() {
        try {

            controller = new SchoolController();
            dataLayer.addSchool(s1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1GetSchools() {
        try {
            this.mockMvc.perform(
                    get("/schools")
            ).andExpect(status().isOk());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test2GetSchool() {
        try {
            MvcResult result = this.mockMvc.perform(
                    get("/schools?name=test")
            ).andExpect(status().isOk()).andReturn();
            School schoolFromController = new School(Document.parse(result.getResponse().getContentAsString()));
            assert(schoolFromController.getAddress().equals("test") && schoolFromController.getName().equals("test"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        dataLayer.deleteSchool(s1);
    }
}

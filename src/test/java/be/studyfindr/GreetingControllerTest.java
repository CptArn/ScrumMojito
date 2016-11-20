package be.studyfindr;

import be.studyfindr.rest.GreetingController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({})
public class GreetingControllerTest {
    private MockMvc mockMvc;

    private GreetingController controller;

    @Before
    public void setUp() {
        controller = new GreetingController();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1IsFound() throws Exception {
        this.mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }
}








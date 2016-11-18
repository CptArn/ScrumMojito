package be.studyfindr;

import be.studyfindr.entities.User;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
    static User u1;

    @BeforeClass
    public static void setUp() {
        u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18);
    }

    @Test
    public void test1GettersAndSetters() {
        u1.setAge(20);
        u1.setEmail("email@gmail.com");
        u1.setFirstname("Jos");
        u1.setLastname("Jansens");
        u1.setLocation("Gent");
        u1.setid(42);

        assert(u1.getAge() == 20 && u1.getEmail() == "email@gmail.com" && u1.getFirstname() == "Jos" && u1.getLastname() == "Jansens" && u1.getLocation() == "Gent" && u1.getid() == 42);
    }
}

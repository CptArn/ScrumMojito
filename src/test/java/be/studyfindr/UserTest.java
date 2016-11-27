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
        u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18, false, false, false, 16, 50, 30, 1, false, false);
    }

    @Test
    public void test1GettersAndSetters() {
        u1.setAge(20);
        u1.setEmail("email@gmail.com");
        u1.setFirstname("Jos");
        u1.setLastname("Jansens");
        u1.setLocation("Gent");
        u1.setid(42);
        u1.setPrefMale(true);
        u1.setPrefFemale(true);
        u1.setPrefTrans(true);
        u1.setPrefAgeMin(18);
        u1.setPrefAgeMax(35);
        u1.setPrefDistance(25);
        u1.setPrefLocation(2);

        assert(u1.getAge() == 20 && u1.getEmail() == "email@gmail.com" && u1.getFirstname() == "Jos" && u1.getLastname() == "Jansens" && u1.getLocation() == "Gent"
                && u1.getid() == 42 && u1.getPrefAgeMin() == 18 && u1.getPrefAgeMax() == 35 && u1. getPrefDistance() == 25 && u1.getPrefLocation() == 2);
    }

    @Test
    public void test2Clone() {
        User u2 = u1.clone();
        assert(u2.equals(u1));
    }

    @Test
    public void test3Hash() {
        User u2 = u1.clone();
        u2.setAge(12);
        int hash1 = u1.hashCode();
        int hash2 = u2.hashCode();
        assert(hash1 != hash2);
    }

    @Test
    public void test4SetGender() {
        User u2 = u1.clone();
        u2.setIsFemale();
        boolean res1 = u2.getIsMale() ^ u2.getIsFemale();
        u2.setIsMale();
        boolean res2 = u2.getIsMale() ^ u2.getIsFemale();
        u2.setIsTrans();
        boolean res3 = u2.getIsTrans() ^ u2.getIsFemale();
        assert(res1 && res2 && res3);
    }

    @Test
    public void test5DefaultContruct() {
        User u2 = new User();
        assert(u2.getFirstname() == null);
    }

    @Test
    public void test6ToString() {
        String res = u1.toString();
        assert(res.contains("firstname"));
    }
}

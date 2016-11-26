package be.studyfindr;

import be.studyfindr.entities.Like;
import be.studyfindr.entities.User;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LikeTest {
    public static User u1;
    public static User u2;

    @BeforeClass
    public static void setUp() {
        u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18, false, false, false, 16, 50, 30, 1);
        u2 = new User(19651, "email@email.com", "Jan", "Peeters", "Oiljst", 18, false, false, false, 16, 50, 30, 1);

    }

    @Test
    public void test1CreateLike() {
        Like like = new Like(u1.getid(), u2.getid());
        assert(like.getLikee_Id() == u2.getid());
        assert(like.getLiker_Id() == u1.getid());
    }
}

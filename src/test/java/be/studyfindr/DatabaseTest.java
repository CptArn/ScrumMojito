package be.studyfindr;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import be.studyfindr.entities.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {
	static Data dataLayer;
	static User u1;
	static User u2;
	static Like l;

	@BeforeClass
	public static void setUp() {
		dataLayer = new Data();
		// dataLayer.deleteAllUsers();
		u1 = new User(19650, "email@email.com", "Jan", "Peeters", 18, true, false, false, 18, 35, 10, 1, false, false, 0.0, 0.0, "gent");
		u2 = new User(98653, "email@email.com", "Bert", "Van Den Borre", 21, true, false, false, 20, 25, 15, 2, false, false, 0.0, 0.0, "gent");
		l = new Like(1, 2, true, false);
	}

	@Test
	public void test1AddUsers() {
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		User return1 = dataLayer.getUser(u1.getid());
		User return2 = dataLayer.getUser(u2.getid());
		assert(return1.equals(u1) && return2.equals(u2));
	}

	@Test
	public void test2DuplicateUsers() {
		int before = dataLayer.getCollectionDocuments("users").size();
		dataLayer.addUser(u1);
		int after = dataLayer.getCollectionDocuments("users").size();
		assertEquals(before, after);
	}

	@Test
	public void test3UpdateUsers() {
		u1.setFirstname("nieuwenaam");
		u2.setEmail("nieuweemail@email.com");
		dataLayer.updateUser(u1);
		dataLayer.updateUser(u2);
		User return1 = dataLayer.getUser(u1.getid());
		User return2 = dataLayer.getUser(u2.getid());
		assert(return1.equals(u1) && return2.equals(u2));
	}

	@Test(expected=IllegalArgumentException.class)
	public void test4DeleteUsers() {
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		User return1 = dataLayer.getUser(u1.getid());
		fail(return1.getEmail());
	}

	@Test
	public void test09AddLike() {
		dataLayer.addLike(l);
		Like found = dataLayer.getLike((long)1, (long)2);
		assert(found.getLiker_Id() == 1);
		assert(found.getLikee_Id() == 2);
	}

	@Test
	public void test10UpdateLike() {
		l.setStatus(true);
		dataLayer.updateLike(l);
		Like found = dataLayer.getLike((long)1, (long)2);
		assert(found.getStatus() == true);

	}

	@Test(expected=IllegalArgumentException.class)
	public  void test11DeleteLike() {
		Like found = dataLayer.getLike((long)1, (long)2);
		dataLayer.deleteLike(found);
		found = dataLayer.getLike((long)1, (long)2);
		assertNull(found);
		// older version Test
		//assert(found.getLiker_Id() == 0);
		//assert(found.getLikee_Id() == 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public  void test11InvalidUser() {
		assert(dataLayer.getUser(-1) == null);
	}

	@Test
	public  void test12GetAllUsers() {
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		List<User> allUsers = dataLayer.getAllUsers();
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		assert(allUsers.contains(u1) && allUsers.contains(u2));
	}

	/*@Test
	public  void test13RemoveAllUsers() {
		List<User> allUsers = dataLayer.getAllUsers();
		dataLayer.deleteAllUsers();
		List<User> emptyList = dataLayer.getAllUsers();
		for (User u:allUsers
			 ) {
			dataLayer.addUser(u);
		}
		assert(emptyList != null && emptyList.size() == 0);
	}*/

	@Test
	public  void test14AddMessage() {
		Message m = new Message("message", new Date(), 1, 2);
		long messId = dataLayer.addMessage(m);
		Message message = dataLayer.getMessage(messId);
		assert(m.equals(message));
		dataLayer.deleteMessage(messId);
	}

	@Test
	public void test15MatchTestNoMatches(){
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		assert(dataLayer.getMatches(u1.getid()).size() == 0);
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
	}

	@Test
	public void test16MatchLikeLike(){
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		Like l1 = new Like(u1.getid(), u2.getid(), true, true);
		Like l2 = new Like(u2.getid(), u1.getid(), true, true);
		dataLayer.addLike(l1);
		dataLayer.addLike(l2);
		assert(dataLayer.getMatches(u1.getid()).size() == 1);
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		dataLayer.deleteLike(l1);
		dataLayer.deleteLike(l2);
	}

	@Test
	public void test17MatchLikeDislike() {
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		Like l1 = new Like(u1.getid(), u2.getid(), true, true);
		Like l2 = new Like(u2.getid(), u1.getid(), false, true);
		dataLayer.addLike(l1);
		dataLayer.addLike(l2);
		System.out.println(dataLayer.getMatches(u1.getid()));
		assert(dataLayer.getMatches(u1.getid()).size() == 0);
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		dataLayer.deleteLike(l1);
		dataLayer.deleteLike(l2);
	}

    @Test
    public void test18MatchLikeChanged() {
        dataLayer.addUser(u1);
        dataLayer.addUser(u2);
        Like l1 = new Like(u1.getid(), u2.getid(), true, true);
        Like l2 = new Like(u2.getid(), u1.getid(), true, true);
        dataLayer.addLike(l1);
        dataLayer.addLike(l2);
        assert(dataLayer.getMatches(u1.getid()).size() == 1);
        l2 = new Like(u2.getid(), u1.getid(), false, true);
        dataLayer.updateLike(l2);
        assert(dataLayer.getMatches(u1.getid()).size() == 0);
        dataLayer.deleteUser(u1);
        dataLayer.deleteUser(u2);
        dataLayer.deleteLike(l1);
        dataLayer.deleteLike(l2);
    }

	/*@Test
	public void test7PopulateLatLon() {
		//double test = ThreadLocalRandom.current().nextDouble(50.5, 51.5);
		List<User> allUsers = dataLayer.getAllUsers();
		for (User user : allUsers) {
			user.setLat(ThreadLocalRandom.current().nextDouble(50.5, 51.5));
			user.setLon(ThreadLocalRandom.current().nextDouble(3, 4));
			dataLayer.updateUser(user);
		}
	}*/
}

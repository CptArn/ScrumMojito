package be.studyfindr;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import be.studyfindr.entities.*;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {
	static Data dataLayer;
	static User u1;
	static User u2;
	static School s1;
	static School s2;


	@BeforeClass
	public static void setUp() {
		dataLayer = new Data();
		// dataLayer.deleteAllUsers();
		u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18);
		u2 = new User(98653, "email@email.com", "Bert", "Van Den Borre", "Gentj", 21);
		s1 = new School("Odisee", "Gebroeders de smet straat 1 Gent");
		s2 = new School("KULeuven", "Oude Markt 13");
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

	@Test(expected=NullPointerException.class)
	public void test4DeleteUsers() {
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		User return1 = dataLayer.getUser(u1.getid());
		fail(return1.getEmail());
	}

	@Test
	public void test5AddSchool() {
		dataLayer.addSchool(s1);
		dataLayer.addSchool(s2);

		School return1 = dataLayer.getSchool(s1);
		School return2 = dataLayer.getSchool(s2);
		s1.setId(return1.getId());
		s2.setId(return2.getId());
		assert(return1.equals(s1) && return2.equals(s2));
	}

	@Test(expected=NullPointerException.class)
	public void test6DeleteSchool() {
		dataLayer.deleteSchool(s1);
		dataLayer.deleteSchool(s2);
		School school = dataLayer.getSchool(s1);
		fail(school.getName());
	}
}

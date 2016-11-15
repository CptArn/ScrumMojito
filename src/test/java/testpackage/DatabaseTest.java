package testpackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import Entities.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {
	static Data dataLayer;
	static User u1;
	static User u2;
	
	
	@BeforeClass
	public static void setUp() {
		dataLayer = new Data();
		// dataLayer.deleteAllUsers();
		u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18);
		u2 = new User(98653, "email@email.com", "Bert", "Van Den Borre", "Gentj", 21);
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
}

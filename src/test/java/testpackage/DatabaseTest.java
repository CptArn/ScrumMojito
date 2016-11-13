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
		u1 = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 18);
		u2 = new User(98653, "email@email.com", "Bert", "Van Den Borre", "Gentj", 21);
	}
	
	@Test
	public void test1AddUsers() {
		dataLayer.addUser(u1);
		dataLayer.addUser(u2);
		User return1 = dataLayer.getUser(u1.getFacebook_id());
		User return2 = dataLayer.getUser(u2.getFacebook_id());
		assert(return1.equals(u1) && return2.equals(u2));
	}
	
	@Test
	public void test2UpdateUsers() {
		u1.setFirstname("nieuwenaam");
		u2.setEmail("nieuweemail@email.com");
		dataLayer.updateUser(u1);
		dataLayer.updateUser(u2);
		User return1 = dataLayer.getUser(u1.getFacebook_id());
		User return2 = dataLayer.getUser(u2.getFacebook_id());
		assert(return1.equals(u1) && return2.equals(u2));
	}
	
	@Test(expected=NullPointerException.class)
	public void test3DeleteUsers() {
		dataLayer.deleteUser(u1);
		dataLayer.deleteUser(u2);
		User return1 = dataLayer.getUser(u1.getFacebook_id());
		fail(return1.getEmail());
	}
}

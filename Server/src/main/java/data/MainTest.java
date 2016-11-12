package data;

import org.bson.Document;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Data d = new Data();
	User u = new User(19650, "email@email.com", "Jan", "Peeters", "Oiljst", 99);
	User u2 = new User(98653, "liame@email.com", "Bert", "Van Den Borre", "Gentj", 32);
	d.addUser(u);
	d.addUser(u2);	
	List<Document> documents = d.getCollectionDocuments("users");
	for (Document document : documents) {
	    System.out.println(document);
	}

	u.setFirstname("nieuwenaam");
	u.setEmail("nieuweemail@email.com");
	d.updateUser(u);
	System.out.println(u);

	d.deleteUser(u2);
	documents = d.getCollectionDocuments("users");
	for (Document document : documents) {
	    System.out.println(document);
	}
	d.deleteUser(u);
    }

}

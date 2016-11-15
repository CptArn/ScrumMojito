package Entities;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Data {
    private MongoClient client;
    private MongoDatabase db;
    
    public Data() {
	client = new MongoClient(new MongoClientURI("mongodb://admin:scrum@ds147497.mlab.com:47497/mojito"));
        db = client.getDatabase("mojito");
    }
    
    public void addUser(User u) {
    Bson filter = new Document("facebook_id", u.getid());
    Document found = db.getCollection("users").find(filter).first();
    if (found == null) {
    	MongoCollection<Document> coll = db.getCollection("users");
    	Document d = new Document("_id", u.getid())
    		.append("email", u.getEmail())
            .append("firstname", u.getFirstname())
            .append("lastname", u.getLastname())
            .append("location", u.getLocation())
            .append("age", u.getAge());
    	coll.insertOne(d);
    }
    }
    
    public void addMessage(Message m) {
	MongoCollection<Document> coll = db.getCollection("messages");
	Document d = new Document("_id", coll.count() + 1)
		.append("message", m.getMessage())
		.append("date", m.getDate())
                .append("status", m.getStatus())
                .append("sender_Id", m.getSender_Id())
                .append("receiver_Id", m.getReceiver_Id());
	coll.insertOne(d);
    }
    
    public void addPhoto(Photo p) {
	MongoCollection<Document> coll = db.getCollection("photos");
	Document d = new Document("_id", coll.count() + 1)
		.append("user_Id", p.getUser_id())
		.append("path", p.getPath());
	coll.insertOne(d);
    }
    
    public void addMatch(Match m) {
	MongoCollection<Document> coll = db.getCollection("matches");
	Document d = new Document("user1_Id", m.getUser1_Id())
		.append("user2_Id", m.getUser2_Id());
	coll.insertOne(d);
    }
    
    public void addSchool(School s) {
	MongoCollection<Document> coll = db.getCollection("schools");
	Document d = new Document("_id", coll.count() + 1)
		.append("name", s.getName())
		.append("name", s.getAddress());
	coll.insertOne(d);
    }
    
    public List<Document> getCollectionDocuments(String collection) {
	MongoCollection<Document> coll = db.getCollection(collection);
	List<Document> documents = (List<Document>) coll.find().into(
		new ArrayList<Document>());
	return documents;
    }
    
    public void updateUser(User u) {
	MongoCollection<Document> coll = db.getCollection("users");
	Bson filter = new Document("_id", u.getid());
	Bson newValue = new Document("_id", u.getid())
		.append("email", u.getEmail())
                .append("firstname", u.getFirstname())
                .append("lastname", u.getLastname())
                .append("location", u.getLocation())
                .append("age", u.getAge());
	Bson updateOperationDocument = new Document("$set", newValue);
	coll.updateOne(filter, updateOperationDocument);
    }
    
    public void deleteUser(User u) {
	MongoCollection<Document> coll = db.getCollection("users");
	Bson filter = new Document("_id", u.getid());
	coll.deleteOne(filter);
    }
    
    public User getUser(int id) {
    	Bson filter = new Document("_id", id);
    	Document doc = db.getCollection("users").find(filter).first();
    	return new User(doc);
    }
    
    public void deleteAllUsers() {
    	db.getCollection("users").deleteMany(new BasicDBObject());
    }
    
    
    /*List<Document> documents = (List<Document>) coll.find().into(
		new ArrayList<Document>());

    for(Document document : documents){
       System.out.println(document);
    }*/
	/*BasicDBObject u = new BasicDBObject("user", "testdb")
		.append("firstname", user.getFirstname())
		.append("lastname", user.getLastname());
	coll.insertOne(u);*/

}

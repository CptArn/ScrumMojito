package be.studyfindr.entities;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public class Data {
	private MongoClient client;
	private MongoDatabase db;

	public Data() {
		client = new MongoClient(new MongoClientURI("mongodb://admin:scrum@ds147497.mlab.com:47497/mojito"));
		db = client.getDatabase("mojito");
	}

	public void addUser(User u) {
		Bson filter = new Document("_id", u.getid());
		Document found = db.getCollection("users").find(filter).first();
		if (found == null) {
			MongoCollection<Document> coll = db.getCollection("users");
			Document d = new Document("_id", u.getid())
					.append("email", u.getEmail())
					.append("firstname", u.getFirstname())
					.append("lastname", u.getLastname())
					.append("location", u.getLocation())
					.append("age", u.getAge())
					.append("prefMale", u.getPrefMale())
					.append("prefFemale", u.getPrefFemale())
					.append("prefTrans", u.getPrefTrans())
					.append("prefAgeMin", u.getPrefAgeMin())
					.append("prefAgeMax", u.getPrefAgeMax())
					.append("prefDistance", u.getPrefDistance())
					.append("prefLocation", u.getPrefLocation())
					.append("male", u.getIsMale())
					.append("female", u.getIsFemale());
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

	public void addLike(Like l) {
		Bson filter = new Document("liker_id", l.getLiker_Id())
				.append("likee_id", l.getLikee_Id());
		Document found = db.getCollection("likes").find(filter).first();
		if (found == null && l.getLikee_Id() != l.getLiker_Id()) {
			MongoCollection<Document> coll = db.getCollection("likes");
			Document d = new Document("liker_id", l.getLiker_Id())
					.append("likee_id", l.getLikee_Id())
					.append("_id", coll.count());
			coll.insertOne(d);
		}
	}

	public void addSchool(School s) {
		MongoCollection<Document> coll = db.getCollection("schools");
		Bson filter = new Document("name", s.getName());
		Document found = coll.find(filter).first();
		if (found == null) {
			Document d = new Document("_id", (int)(coll.count() + 1))
					.append("name", s.getName())
					.append("address", s.getAddress());
			coll.insertOne(d);
		}
	}

	public List<Document> getCollectionDocuments(String collection) {
		MongoCollection<Document> coll = db.getCollection(collection);
		List<Document> documents = (List<Document>) coll.find().into(
				new ArrayList<Document>());
		return documents;
	}

	public void updateUser(User u) {
		MongoCollection<Document> collection = db.getCollection("users");
		collection.updateOne(eq("_id", u.getid()), new Document("$set",
				new Document("email", u.getEmail())
						.append("firstname", u.getFirstname())
						.append("lastname", u.getLastname())
						.append("location", u.getLocation())
						.append("age", u.getAge())
						.append("prefMale", u.getPrefMale())
						.append("prefFemale", u.getPrefFemale())
						.append("prefTrans", u.getPrefTrans())
						.append("prefAgeMin", u.getPrefAgeMin())
						.append("prefAgeMax", u.getPrefAgeMax())
						.append("prefDistance", u.getPrefDistance())
						.append("prefLocation", u.getPrefLocation())
						.append("male", u.getIsMale())
						.append("female", u.getIsFemale())
		));
	}

	public void deleteUser(User u) {
		MongoCollection<Document> coll = db.getCollection("users");
		Bson filter = new Document("_id", u.getid());
		coll.deleteOne(filter);
	}

	public User getUser(long id) {
		Bson filter = new Document("_id", id);
		Document doc;
		try{
			doc = db.getCollection("users").find(filter).first();
		}catch(Exception ex){
			throw new IllegalArgumentException("Invalid ID.");
		}
		return new User(doc);
	}

	public void deleteAllUsers() {
		db.getCollection("users").deleteMany(new BasicDBObject());
	}

	public List<School> getAllSchools() {
		List<School> found = new ArrayList<School>();
		FindIterable<Document> returnedSchools = db.getCollection("schools").find();
		for(Document doc : returnedSchools) {
			found.add(new School(doc));
		}
		return found;
	}

	public School getSchool(String schoolName) {
		School found;
		Bson filter = new Document("name", schoolName);
		Document objectFound = db.getCollection("schools").find(filter).first();
		found = new School(objectFound);
		return found;
	}

	public void updateSchool(School s) {
		MongoCollection<Document> coll = db.getCollection("schools");
		Bson filter = new Document("_id", s.getId());
		Bson newValue = new Document("_id", s.getId())
				.append("name", s.getName())
				.append("address", s.getAddress());
		Bson updateOperationDocument = new Document("$set", newValue);
		coll.updateOne(filter, updateOperationDocument);
	}

	public void deleteSchool(School school) {
		MongoCollection<Document> coll = db.getCollection("schools");
		Bson filter = new Document("name", school.getName());
		coll.deleteOne(filter);
	}

	public Like getLike(Long liker_id, Long likee_id) {
		Like found;
		Bson filter = new Document("liker_id", liker_id).append("likee_id", likee_id);
		Document objectFound = db.getCollection("likes").find(filter).first();
		found = new Like(objectFound);
		return found;
	}

	public void getLikesByLiker() {

	}

	public void getLikesByLikee() {

	}

	public void deleteLike(Like l) {
		MongoCollection<Document> coll = db.getCollection("likes");
		Bson filter = new Document("liker_id", l.getLiker_Id()).append("likee_id", l.getLikee_Id());
		coll.deleteOne(filter);
	}
}

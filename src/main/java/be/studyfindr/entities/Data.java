package be.studyfindr.entities;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Data is the interface between the logic and the StudyFinder database.
 */
public class Data {

	// MongoDB client
	private MongoClient client;

	// MongoDB database interface
	private MongoDatabase db;

	/**
	 * Creates an instance of the database interface.
	 */
	public Data() {
		client = new MongoClient(new MongoClientURI("mongodb://admin:scrum@ds147497.mlab.com:47497/mojito"));
		db = client.getDatabase("mojito");
	}

	/**
	 * Adds a new user to the database.
	 * @param u the user to add
	 */
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

	/**
	 * Adds a new message to the database
	 * @param m message to add
	 */
	public void addMessage(Message m) {
		Bson filter = new Document("_id", m.getId());
		Document found = db.getCollection("messages").find(filter).first();
		if (found == null) {
			MongoCollection<Document> coll = db.getCollection("messages");
			Document d = new Document("_id", m.getId())
					.append("message", m.getMessage())
					.append("date", m.getDate())
					.append("status", m.getStatus())
					.append("sender_Id", m.getSender_Id())
					.append("receiver_Id", m.getReceiver_Id());
			coll.insertOne(d);
		}
	}

	/**
	 * Deletes a message from the database based on the message id.
	 * @param id id of the message to remove.
	 */
	public void deleteMessage(long id){
		MongoCollection<Document> coll = db.getCollection("messages");
		Bson filter = new Document("_id", id);
		coll.deleteOne(filter);
	}

	public List<Message> getMessages(int id){
		List<Message> messages = new ArrayList<Message>();
		db.getCollection("messages").find().forEach((Block<? super Document>) (e) -> messages.add(new Message(e)));
		return messages;
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
					.append("_id", coll.count())
					.append("confirmed", l.getStatus())
					.append("like", l.getLike());
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
		doc = db.getCollection("users").find(filter).first();
		return new User(doc);
	}

	public User getUser(long id, User user_pref) {
		Bson filter = new Document("_id", id);
		Document doc;
		doc = db.getCollection("users").find(and(
				eq("_id", id),
				lte("age", user_pref.getPrefAgeMax()),
				gte("age", user_pref.getPrefAgeMin()),
				eq("male", user_pref.getPrefMale()),
				eq("female", user_pref.getPrefFemale())
		)).first();
		if (doc == null) return null;
		return new User(doc);
	}

	public List<User> getAllUsers() {
		MongoCollection<Document> doc = db.getCollection("users");
		List<User> users = new ArrayList<User>();
		db.getCollection("users").find().forEach((Block<? super Document>) (e) -> e.equals(users.add(new User(e))));
		return users;
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
		try {
			found = new Like(objectFound);
		}catch(Exception ex){
			throw ex;
		}
		return found;
	}

	public void updateLike(Like l) {
		MongoCollection<Document> coll = db.getCollection("likes");
		Bson filter = new Document("liker_id", l.getLiker_Id()).append("likee_id", l.getLikee_Id());
		Bson newValue = new Document("liker_id", l.getLiker_Id())
				.append("likee_id", l.getLikee_Id())
				.append("confirmed", l.getStatus())
				.append("like", l.getLike());
		Bson updateOperationDocument = new Document("$set", newValue);
		coll.updateOne(filter, updateOperationDocument);
	}

	public List<User> getLikesByLikee(User current_user) {
		Bson filter = new Document("likee_id", current_user.getid())
				.append("confirmed", false)
				.append("like", true);
		FindIterable<Document> documents = db.getCollection("likes").find(filter);
		List<User> foundUsers = new ArrayList<>();
		documents.forEach((Block<? super Document>) (e) -> {
			try{
				foundUsers.add(getUser(e.getLong("liker_id"), current_user));
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		});
		/*for(Document doc : documents) {
			User u = getUser(doc.getLong("liker_id"), current_user);
			if (u != null) {
				foundUsers.add(u);
			}
		}*/
		return foundUsers;
	}

	public List<User> getNotLikedUsers(User current_user) {
		Bson filter = new Document("liker_id", current_user.getid())
				.append("like", true);
		FindIterable<Document> likes_docs = db.getCollection("likes").find(filter).sort(new Document("_id", -1));
		List<Long> likees = new ArrayList<>();
		likes_docs.forEach((Block<? super Document>) (like_doc) -> {
			try{
				likees.add(like_doc.getLong("likee_id"));
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		});

		FindIterable<Document> users = db.getCollection("users").find().sort(new Document("_id", -1));
		List<User> foundUsers = new ArrayList<>();
		users.forEach((Block<? super Document>) (user_doc) -> {
			try{
				String q = user_doc.containsKey("_id") ? "_id" : "id";
				if (!likees.contains(user_doc.getLong(q))) foundUsers.add(new User(user_doc));
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		});
		if (foundUsers.contains(current_user)) {
			foundUsers.remove(current_user);
		}

		/*for(Document doc : documents) {
			Like l = getLike(current_user.getid(), doc.getLong("_id"));
			if (l == null) {
				User u = getUser(doc.getLong("_id"), current_user);
				if (u != null) {
					foundUsers.add(u);
				}
			}
		}*/
		return foundUsers;
	}

	public void deleteLike(Like l) {
		MongoCollection<Document> coll = db.getCollection("likes");
		Bson filter = new Document("liker_id", l.getLiker_Id()).append("likee_id", l.getLikee_Id());
		coll.deleteOne(filter);
	}

	public List<User> getQueue(Long user_id) {
		User current_user = getUser(user_id);
		List<User> queue = new ArrayList<>();
		List<User> temp = getLikesByLikee(current_user);
		if (temp.size() > 0) queue.addAll(temp);
		temp = getNotLikedUsers(current_user);
		if (temp.size() > 0) queue.addAll(temp);
		queue.removeAll(Collections.singleton(null));
		return queue;
	}

	public List<Like> getLikesByLiker(long id){
		List<Like> likes = new ArrayList<>();
		Bson filter = new Document("liker_id", id);
		db.getCollection("likes").find(filter).forEach((Block<? super Document>) (e) -> likes.add(new Like(e)));
		return likes;
	}

	public List<User> getMatches(long id){
		List<User> matches = new ArrayList<>();
		List<Like> likes = getLikesByLiker(id);
		likes.forEach((like) -> {
			try{
				// get inverse like (IF is optional but nice to have)			and add likee to collection
				if (getLike(like.getLikee_Id(), like.getLiker_Id()) != null) matches.add(getUser(like.getLikee_Id()));

			}catch(Exception ex){
				// no inverse like
			}
		});
		return matches;
	}
}

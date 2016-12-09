package be.studyfindr.entities;

import be.studyfindr.rest.Util;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.stream.Collectors;

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
					.append("female", u.getIsFemale())
					.append("lat", u.getLat())
					.append("lon", u.getLon());
			coll.insertOne(d);
		}
	}

	/**
	 * Adds a new message to the database
	 * @param m message to add
	 */
	public long addMessage(Message m) {
		try{
			getMessage(m);
			// message exists
			throw new IllegalArgumentException("Same message with same timestamp already exists");
		}catch(Exception ex){

		}
		long id;
		MongoCollection<Document> coll = db.getCollection("messages");
		id = coll.count();
		Document d = new Document("_id", coll.count())
				.append("message", m.getMessage())
				.append("date", m.getDate())
				.append("sender_Id", m.getSender_Id())
				.append("receiver_Id", m.getReceiver_Id());
		coll.insertOne(d);
		return id;
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

	/**
	 * Returns all messages in database
	 * @return all messages
	 */
	public List<Message> getAllMessages(){
		List<Message> messages = new ArrayList<Message>();
		db.getCollection("messages").find().forEach((Block<? super Document>) (e) -> messages.add(new Message(e)));
		return messages;
	}

	/**
	 * Returns all messages between 2 users
	 * @param my_id own id
	 * @param other_id other id
	 * @return messages between users
	 */
	public List<Message> getMessages(long my_id, long other_id){
		List<Message> messages = new ArrayList<Message>();
		Bson f1 = new Document("sender_Id", my_id).append("receiver_Id", other_id);
		Bson f2 = new Document("sender_Id", other_id).append("receiver_Id", my_id);
		db.getCollection("messages").find(or(f1, f2)).sort(new BasicDBObject("_id",-1)).forEach((Block<? super Document>) (e) -> messages.add(new Message(e)));
		return messages;
	}

	/**
	 * Gets a message based on message id
	 * @param id message id
	 * @return message
	 */
	public Message getMessage(long id){
		Bson f1 = new Document("_id", id);
		Document doc = db.getCollection("messages").find(f1).first();
		return new Message(doc);
	}

	/**
	 * Reloads a message from database
	 * @param message message to find
	 * @return message from database
	 */
	public Message getMessage(Message message){
		Bson f1 = new Document("message", message.getMessage()).append("date", message.getDate()).append("sender_Id", message.getSender_Id()).append("receiver_Id", message.getReceiver_Id());
		Document doc = db.getCollection("messages").find(f1).first();
		return new Message(doc);
	}

	/**
	 * Adds a match to the database
	 * @param m match to add
	 */
	public void addMatch(Match m) {
		MongoCollection<Document> coll = db.getCollection("matches");
		Document d = new Document("liker_Id", m.getLiker_Id())
				.append("likee_Id", m.getLikee_Id());
		coll.insertOne(d);
	}

	/**
	 * Deletes a match from database
	 * @param m match to delete
	 */
	public void deleteMatch(Match m){
		MongoCollection<Document> coll = db.getCollection("matches");
		Bson filter = new Document("liker_Id", m.getLiker_Id())
				.append("likee_Id", m.getLikee_Id());
		coll.deleteOne(filter);
	}

	/**
	 * Adds a like to the database
	 * @param l like to add
	 */
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

	/**
	 * Adds a school to the database
	 * @param s school to add
	 */
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

	/**
	 * Gets a list of documents, named 'collection' based on a collection name
	 * @param collection collection name
	 * @return collection
	 */
	public List<Document> getCollectionDocuments(String collection) {
		MongoCollection<Document> coll = db.getCollection(collection);
		List<Document> documents = (List<Document>) coll.find().into(
				new ArrayList<Document>());
		return documents;
	}

	/**
	 * Updates a user in database
	 * @param u user object with new information
	 */
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

	/**
	 * Deletes a user from database based on the user object
	 * @param u user object to remove
	 */
	public void deleteUser(User u) {
		MongoCollection<Document> coll = db.getCollection("users");
		Bson filter = new Document("_id", u.getid());
		coll.deleteOne(filter);
	}

	/**
	 * Returns a user from database based on the user id
	 * @param id user id to find
	 * @return the found user
	 */
	public User getUser(long id) {
		Bson filter = new Document("_id", id);
		Document doc;
		doc = db.getCollection("users").find(filter).first();
		return new User(doc);
	}

	/**
	 * Returns a user from database based on the id of the user and a user object without user id.
	 * @param id user id to find
	 * @param user_pref object containing preferences
	 * @return the found user, null if not present
	 */
	public User getUser(long id, User user_pref) {
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

	/**
	 * Returns all users from database
	 * @return
	 */
	public List<User> getAllUsers() {
		MongoCollection<Document> doc = db.getCollection("users");
		List<User> users = new ArrayList<User>();
		db.getCollection("users").find().forEach((Block<? super Document>) (e) -> users.add(new User(e)));
		return users;
	}

	/**
	 * Deletes all users in database
	 */
	public void deleteAllUsers() {
		db.getCollection("users").deleteMany(new BasicDBObject());
	}

	/**
	 * Returns all schools from database
	 * @return all schools from database
	 */
	public List<School> getAllSchools() {
		List<School> found = new ArrayList<School>();
		FindIterable<Document> returnedSchools = db.getCollection("schools").find();
		for(Document doc : returnedSchools) {
			found.add(new School(doc));
		}
		return found;
	}

	/**
	 * Gets a school from database by name
	 * @param schoolName name of school to find
	 * @return school from database
	 */
	public School getSchool(String schoolName) {
		School found;
		Bson filter = new Document("name", schoolName);
		Document objectFound = db.getCollection("schools").find(filter).first();
		found = new School(objectFound);
		return found;
	}

	/**
	 * Updates a school in database
	 * @param s school to update
	 */
	public void updateSchool(School s) {
		MongoCollection<Document> coll = db.getCollection("schools");
		Bson filter = new Document("_id", s.getId());
		Bson newValue = new Document("_id", s.getId())
				.append("name", s.getName())
				.append("address", s.getAddress());
		Bson updateOperationDocument = new Document("$set", newValue);
		coll.updateOne(filter, updateOperationDocument);
	}

	/**
	 * Deletes a school from database
	 * @param school school to remove
	 */
	public void deleteSchool(School school) {
		MongoCollection<Document> coll = db.getCollection("schools");
		Bson filter = new Document("name", school.getName());
		coll.deleteOne(filter);
	}

	/**
	 * Returns a like from database based on liker & likee id
	 * @param liker_id liker id
	 * @param likee_id likee id
	 * @return found like
	 */
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

	/**
	 * Update an existing like in database
	 * @param l like to update
	 */
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

	/**
	 * Returns all likes based on likee
	 * @param current_user likee
	 * @return list of found likes
	 */
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
		return foundUsers;
	}

	/**
	 * Returns a list of not liked user for a certain user
	 * @param current_user the user for which we search the not liked users
	 * @return list of not liked users
	 */
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
		return foundUsers;
	}

	/**
	 * Gets a list of disliked user ids
	 * @param current_user own user
	 * @return list of disliked user ids
	 */
	public List<Long> getDislikedIds(User current_user){
		Bson filter = new Document("liker_id", current_user.getid())
				.append("like", false);
		FindIterable<Document> disliked_docs = db.getCollection("likes").find(filter).sort(new Document("_id", -1));
		List<Long> disliked_users = new ArrayList<>();
		disliked_docs.forEach((Block<? super Document>) (like_doc) -> {
			try{
				disliked_users.add(like_doc.getLong("likee_id"));
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		});
		return disliked_users;
	}

	/**
	 * Deletes a like from database based on the like
	 * @param l like to remove
	 */
	public void deleteLike(Like l) {
		MongoCollection<Document> coll = db.getCollection("likes");
		Bson filter = new Document("liker_id", l.getLiker_Id()).append("likee_id", l.getLikee_Id());
		coll.deleteOne(filter);
	}

	/**
	 * Returns a list of candidates for a user
	 * @param user_id user
	 * @return list of candidates
	 */
	public List<User> getQueue(Long user_id){
		List<Long> disliked = getDislikedIds(getUser(user_id));
		User current_user = getUser(user_id);
		Set<User> candidates = new HashSet<>();
		getNotLikedUsers(getUser(user_id)).forEach((user) -> candidates.add(user));
		return candidates.stream()
				.filter((user) -> {
					// filter age
					if (user == null || user.equals(current_user)) return false;
					// check first direction
					if (user.getAge() >= current_user.getPrefAgeMin() && user.getAge() <= current_user.getPrefAgeMax()){
						// check inverse
						boolean s = (current_user.getAge() >= user.getPrefAgeMin()) && (current_user.getAge() <= user.getPrefAgeMax());
						return s;
					}
					return false;
				})
				.filter((user) -> {
					// filter gender
					// if gender is unknown
					if (current_user.getIsGenderUnknown()){
						return user.getIsGenderUnknown() ||
								(current_user.getPrefFemale() && user.getIsFemale()) ||
								(current_user.getPrefMale() && user.getIsMale()) ||
								(current_user.getPrefTrans() && user.getIsTrans());
					}
					// gender is known
					boolean s1 = (user.getIsFemale() && current_user.getPrefFemale()) ||
							(user.getIsMale() && current_user.getPrefMale()) ||
							(user.getIsTrans() && current_user.getPrefTrans());
					boolean s2 = (current_user.getIsFemale() && user.getPrefFemale()) ||
							(current_user.getIsMale() && user.getPrefMale()) ||
							(current_user.getIsTrans() && user.getPrefTrans());
					return s1 && s2;
				})
				.filter((user) -> {
					// filter distance (one direction)
					if ((current_user.getLat()) == 0.0 && (current_user.getLon() == 0.0)) return true;
					if ((user.getLat()) == 0.0 && (user.getLon() == 0.0)) return true;
					boolean s = new Util().usersAreInRange(current_user, user);
					return s;
				})
				.filter((user) -> {
					// remove disliked users
					boolean s = !disliked.contains(user.getid());
					return s;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Gets a likes for by liker
	 * @param id liker id
	 * @return all likes for likers
	 */
	public List<Like> getLikesByLiker(long id){
		List<Like> likes = new ArrayList<>();
		Bson filter = new Document("liker_id", id);
		db.getCollection("likes").find(filter).forEach((Block<? super Document>) (e) -> likes.add(new Like(e)));
		return likes;
	}

	/**
	 * Returns all matches for a user.
	 * @param id user id
	 * @return list of matches
	 */
	public List<User> getMatches(long id){
		List<User> matches = new ArrayList<>();
		List<Like> likes = getLikesByLiker(id);
		likes.forEach((like) -> {
            Like like1, like2;
			try{
				// get inverse like (IF is optional but nice to have)			and add likee to collection
                like1 = getLike(like.getLikee_Id(), like.getLiker_Id());
                like2 = getLike(like.getLiker_Id(), like.getLikee_Id());
				if (like1 != null && like2 != null &&
                    like1.getLike() && like2.getLike()) {
					matches.add(getUser(like.getLikee_Id()));
				}
			}catch(Exception ex){
				// no inverse like
			}
		});
		return matches;
	}

	/**
	 * Checks if users have a match
	 * @param user1_id user id 1
	 * @param user2_id user id 2
	 * @return true if there is a match, false if there is no match
	 */
	public boolean usersHaveMatch(long user1_id, long user2_id){
		try{
			// if no match one of these will fail
			Like l1 = getLike(user1_id, user2_id);
			Like l2 = getLike(user2_id, user1_id);
			return (l1 != null && l2 != null);
		}catch(Exception ex){
			return false;
		}
	}
}

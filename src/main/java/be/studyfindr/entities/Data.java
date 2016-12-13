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
		client = new MongoClient(new MongoClientURI("mongodb://admin:scrum@ds147497.mlab.com:47497/mojito",
				MongoClientOptions.builder()
						.connectionsPerHost(10)
						.threadsAllowedToBlockForConnectionMultiplier(5)
		));
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

		MongoCollection<Document> coll = db.getCollection("messages");
		Document id = db.getCollection("messages").find().sort(new Document("_id", -1)).first();

		Document d = new Document("_id", id.getLong("_id") + 1)
				.append("message", m.getMessage())
				.append("date", m.getDate())
				.append("sender_Id", m.getSender_Id())
				.append("receiver_Id", m.getReceiver_Id());
		coll.insertOne(d);
		return (id.getLong("_id") + 1);
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

	public void deleteConversation(long idUser1, long idUser2){
		MongoCollection<Document> coll = db.getCollection("messages");
		Bson filter1 = new Document("sender_Id", idUser1).append("receiver_Id", idUser2);
		Bson filter2 = new Document("sender_Id", idUser2).append("receiver_Id", idUser1);
		coll.deleteMany(filter1);
		coll.deleteMany(filter2);
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
	 * Adds a like to the database
	 * @param l like to add
	 */
	public void addLike(Like l) {
		Bson filter = new Document("liker_id", l.getLiker_Id())
				.append("likee_id", l.getLikee_Id());
		Document found = db.getCollection("likes").find(filter).first();
		Document id = db.getCollection("likes").find().sort(new Document("_id", -1)).first();

		if (found == null && l.getLikee_Id() != l.getLiker_Id()) {
			MongoCollection<Document> coll = db.getCollection("likes");
			Document d = new Document("liker_id", l.getLiker_Id())
					.append("likee_id", l.getLikee_Id())
					.append("_id", id.getLong("_id") + 1)
					.append("like", l.getLike());
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
						.append("lat", u.getLat())
						.append("lon", u.getLon())
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

	public boolean backendHasUser(long id){
		Bson filter = new Document("_id", id);
		Document doc;
		doc = db.getCollection("users").find(filter).first();
		return doc != null;
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
			found = null;
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
				.append("like", l.getLike());
		Bson updateOperationDocument = new Document("$set", newValue);
		coll.updateOne(filter, updateOperationDocument);
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
					// gender is known
					boolean s1 = (user.getIsFemale() && current_user.getPrefFemale()) ||
							(user.getIsMale() && current_user.getPrefMale()) ||
							(user.getIsTrans() && current_user.getPrefTrans());
					if (user.getIsFemale() || user.getIsMale() || user.getIsTrans()) {
						return s1;
					} else {
						return true;
					}
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

package be.studyfindr.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;

/**
 * Represents a user
 */
public class User {
	/**
	 * The default preferred min. age
	 */
	public final int DEFAULT_PREF_AGE_MIN = 18;

	/**
	 * The default preferred max age
	 */
	public final int DEFAULT_PREF_AGE_MAX = 35;

	/**
	 * The default preferred distance
	 */
	public final int DEFAULT_PREF_DISTANCE = 100;

	/**
	 * Creates a new User.
	 * @param id id of the user obtained from Facebook (facebook user id)
	 * @param email mail of the user
	 * @param firstname first name of the user
	 * @param lastname last name of the user
	 * @param age age of the user
	 * @param prefMale wants to find male
	 * @param prefFemale wants to find female
	 * @param prefTrans wants to find trans.
	 * @param prefAgeMin preferred min. age
	 * @param prefAgeMax preferred max. age
 	 * @param prefDistance preferred max. distance
	 * @param prefLocation preferred location
	 * @param male preferred male
	 * @param female preferred female
	 * @param lat latidude of the user
	 * @param lon longitude of the user
	 */
	public User(long id, String email, String firstname, String lastname, int age,
				boolean prefMale, boolean prefFemale, boolean prefTrans, int prefAgeMin, int prefAgeMax, int prefDistance, int prefLocation, boolean male, boolean female, double lat, double lon, String location) {
		super();
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.location = location;
		this.age = age;
        this.prefMale = prefMale;
        this.prefFemale = prefFemale;
        this.prefTrans = prefTrans;
		this.prefAgeMin = prefAgeMin;
		this.prefAgeMax = prefAgeMax;
		this.prefDistance = prefDistance;
		this.prefLocation = prefLocation;
		this.female = female;
		this.male = male;
		this.lat = lat;
		this.lon = lon;
		this.location = location;
	}

	/**
	 * Parses a user document into a user
	 * @param doc user document
	 */
	public User(Document doc) {
		if (doc == null) throw new IllegalArgumentException("The document cannot be 'null'.");
		String idFieldName;
		if (doc.containsKey("_id")){
			idFieldName = "_id";
		}else if (doc.containsKey("id")){
			idFieldName = "id";
		}else{
			throw new IllegalArgumentException("Cannot find the id.");
		}
		Object idFromDoc = doc.getOrDefault(idFieldName, "");
		if (idFromDoc instanceof Integer){
			this.id = (long) doc.getInteger(idFieldName);
		}
		else{
			this.id = doc.getLong(idFieldName);
		}
		this.email = doc.getString("email");
		this.firstname = doc.getString("firstname");
		this.lastname = doc.getString("lastname");
		this.location = doc.getString("location");
		this.age = doc.getInteger("age");
		this.prefMale = doc.getBoolean("prefMale", true);
		this.prefFemale = doc.getBoolean("prefFemale", true);
		this.prefTrans = doc.getBoolean("prefTrans", true);
		this.prefAgeMin = doc.getInteger("prefAgeMin", DEFAULT_PREF_AGE_MIN);
		this.prefAgeMax = doc.getInteger("prefAgeMax", DEFAULT_PREF_AGE_MAX);
		this.prefDistance = doc.getInteger("prefDistance", DEFAULT_PREF_DISTANCE);
		this.prefLocation = doc.getInteger("prefLocation");
		this.male = doc.getBoolean("male", false);
		this.female = doc.getBoolean("female", false);
		this.lat = doc.containsKey("lat")? doc.getDouble("lat") : 0.0;
		this.lon = doc.containsKey("lon")? doc.getDouble("lon") : 0.0;
	}

	/**
	 * Default constructor.
	 */
	public User() {

	}

	/**
	 * Returns the string representation of the user.
	 * @return
	 */
	@Override
	public String toString() {
		return "User [_id=" + id + ", email=" + email + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", location=" + location + ", age=" + age + ", prefMale=" + prefMale
				+ ", prefFemale=" + prefFemale + ", prefTrans=" + prefTrans + ", prefAgeMin=" + prefAgeMin + ", prefAgeMax=" + prefAgeMax + ", prefDistance="
				+ prefDistance + ", prefLocation=" + prefLocation + ", Lat=" + lat + ", Lon=" + lon + ", male=" + male + ", female=" + female + "]";
	}

	// user vars.
	private long id;
	private String email;
	private String firstname;
	private String lastname;
	private String location;
	private int age;
	private boolean male;
	private boolean female;
	private double lat;
	private double lon;

	// preferences
    private boolean prefMale;
	private boolean prefFemale;
	private boolean prefTrans;
	private int prefAgeMin;
	private int prefAgeMax;
	private int prefDistance;
	private int prefLocation;

	/**
	 * True if male
	 * @return
	 */
	@JsonProperty("male")
	public boolean getIsMale(){
		return this.male && !this.female;
	}

	/**
	 * True if female
	 * @return
	 */
	@JsonProperty("female")
	public boolean getIsFemale(){
		return female && !male;
	}

	/**
	 * True if trans.
	 * @return
	 */
	@JsonProperty("trans")
	public boolean getIsTrans(){
		return male && female;
	}

	public boolean getIsGenderUnknown(){
		return !male && !female;
	}

	/**
	 * Makes the user male
	 */
	public void setIsMale(){
		this.female = false;
		this.male = true;
	}

	/**
	 * Makes the user female
	 */
	public void setIsFemale(){
		this.female = true;
		this.male = false;
	}

	/**
	 * Makes the user trans
	 */
	public void setIsTrans(){
		this.female = true;
		this.male = true;
	}

	/**
	 * Returns the id of the user
	 * @return id of the user
	 */
	public long getid() {
		return id;
	}

	/**
	 * Sets the Facebook id of the user
	 * @param id new id
	 */
	public void setid(long id) {
		this.id = id;
	}

	/**
	 * Returns the email of the user.
	 * @return email of the user
	 */
	public String getEmail() {
		return email == null ? "" : email;
	}

	/**
	 * Sets the email property
	 * @param email new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the first name
	 * @return first name
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Sets the first name
	 * @param firstname new first name
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * Returns the last name
	 * @return last name
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Sets the last name
	 * @param lastname new last name
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Returns the location of the user
	 * @return location of the user
	 */
	public String getLocation() {
		return location == null ? "" : location;
	}

	/**
	 * Sets the location of the user
	 * @param location new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Returns the age of the user
	 * @return age of user
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age of the user
	 * @param age new age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * True if the user prefers male candidates
	 * @return prefers male candidates
	 */
    public boolean getPrefMale() {
        return prefMale;
    }

	/**
	 * Sets the gender preference accordingly
	 * @param preferenceMale user preference
	 */
	public void setPrefMale(boolean preferenceMale) {
        this.prefMale = preferenceMale;
    }

	/**
	 * True if the user prefers female candidates
	 * @return prefers female
	 */
	public boolean getPrefFemale() {
        return prefFemale;
    }

	/**
	 * Sets the gender preference accordingly
	 * @param prefFemale user preference
	 */
	public void setPrefFemale(boolean prefFemale) {
        this.prefFemale = prefFemale;
    }

	/**
	 * True if the user prefers trans. candidates
	 * @return prefers trans. candidates
	 */
	public boolean getPrefTrans() {
        return prefTrans;
    }

	/**
	 * Sets the gender preference accordingly
	 * @param prefTrans user preference
	 */
	public void setPrefTrans(boolean prefTrans) {
        this.prefTrans = prefTrans;
    }

	/**
	 * Returns the min. preferred age for candidates
	 * @return min. preferred age
	 */
	public int getPrefAgeMin() {
		return prefAgeMin;
	}

	/**
	 * Sets the max. preferred age for candidates
	 * @param prefAge max. preferred age
	 */
	public void setPrefAgeMin(int prefAge) {
		this.prefAgeMin = prefAge;
	}

	/**
	 *Returns the preferred max. age
	 * @return preferred max. age
	 */
	public int getPrefAgeMax() {
		return prefAgeMax;
	}

	/**
	 * Sets the max. preferred age for candidates
	 * @param prefAgeMax
	 */
	public void setPrefAgeMax(int prefAgeMax) {
		this.prefAgeMax = prefAgeMax;
	}

	/**
	 * Returns the preferred distance to find candidates
	 * @return preferred distance to find candidates
	 */
	public int getPrefDistance() {
		return prefDistance;
	}

	/**
	 * Sets the preferred distance to find candidates
	 * @param prefDistance preferred distance to find candidates
	 */
	public void setPrefDistance(int prefDistance) {
		this.prefDistance = prefDistance;
	}

	/**
	 * Returns the preferred location to find matches
	 * @return preferred location to find matches
	 */
	public int getPrefLocation() {
		return prefLocation;
	}

	/**
	 * Sets the preferred location to find matches
	 * @param prefLocation preferred location to find matches
	 */
	public void setPrefLocation(int prefLocation) {
		this.prefLocation = prefLocation;
	}

	/**
	 * Sets the latitude of the location of the user
	 * @param lat new latitude
	 */
	public void setLat(long lat){
		this.lat = lat;
	}

	/**
	 * Sets the longitude of the location of the user
	 * @param lon new longitude
	 */
	public void setLon(long lon){
		this.lon = lon;
	}

	/**
	 * Returns the latitude of the location of the user
	 * @return latitude
	 */
	public double getLat(){
		return this.lat;
	}

	/**
	 * Returns the longitude of the location of the user
	 * @return longitude
	 */
	public double getLon(){
		return this.lon;
	}

	/**
	 * Compares the current User object with an other object
	 * @param o other object
	 * @return true if equal (based on object type and properties)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (id != user.id) return false;
		if (getAge() != user.getAge()) return false;
		if (getPrefMale() != user.getPrefMale()) return false;
		if (getPrefFemale() != user.getPrefFemale()) return false;
		if (getPrefTrans() != user.getPrefTrans()) return false;
		if (getPrefAgeMin() != user.getPrefAgeMin()) return false;
		if (getPrefDistance() != user.getPrefDistance()) return false;
		if (getPrefLocation() != user.getPrefLocation()) return false;
		if (!getEmail().equals(user.getEmail())) return false;
		if (!getFirstname().equals(user.getFirstname())) return false;
		if (!getLastname().equals(user.getLastname())) return false;
		if (getIsFemale() != user.getIsFemale()) return false;
		if (getIsMale() != user.getIsMale()) return false;
		if (getIsTrans() != user.getIsTrans()) return false;
		if (!getLocation().equals(user.getLocation())) return false;
		if (getLon() != user.getLon()) return false;
		return getLat() == user.getLat();

	}

	/**
	 * Generates a unique hash for this object
	 * @return hash
	 */
	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + getEmail().hashCode();
		result = 31 * result + getFirstname().hashCode();
		result = 31 * result + getLastname().hashCode();
		result = 31 * result + getLocation().hashCode();
		result = 31 * result + getAge();
		result = 31 * result + (getPrefMale() ? 1 : 0);
		result = 31 * result + (getPrefFemale() ? 1 : 0);
		result = 31 * result + (getPrefTrans() ? 1 : 0);
		result = 31 * result + getPrefAgeMin();
		result = 31 * result + getPrefDistance();
		result = 31 * result + getPrefLocation();
		return result;
	}

	/**
	 * Clones the user.
	 * @return clone of object
	 */
	public User clone(){
		return new User(this.id, this.email, this.firstname, this.lastname, this.age,
		this.prefMale, this.prefFemale, this.prefTrans, this.prefAgeMin, this.prefAgeMax, this.prefDistance, this.prefLocation, this.male, this.female, this.lat, this.lon, this.location);
	}
}

package be.studyfindr.entities;

import org.bson.Document;
//TODO add lat lon to constructor
public class User {
	public final int DEFAULT_PREF_AGE_MIN = 18;
	public final int DEFAULT_PREF_AGE_MAX = 35;
	public final int DEFAULT_PREF_DISTANCE = 100;

	public User(long id, String email, String firstname, String lastname, String location, int age,
				boolean prefMale, boolean prefFemale, boolean prefTrans, int prefAgeMin, int prefAgeMax, int prefDistance, int prefLocation, boolean male, boolean female) {
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
	}

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
	}

	// dummy constructor
	public User() {

	}

	@Override
	public String toString() {
		return "User [_id=" + id + ", email=" + email + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", location=" + location + ", age=" + age + ", prefMale=" + prefMale
				+ ", prefFemale=" + prefFemale + ", prefTrans=" + prefTrans + ", prefAgeMin=" + prefAgeMin + ", prefAgeMax=" + prefAgeMax + ", prefDistance="
				+ prefDistance + ", prefLocation=" + prefLocation + "]";
	}

	private long id;
	private String email;
	private String firstname;
	private String lastname;
	private String location;
	private int age;
	private boolean male;
	private boolean female;
	private long lat;
	private long lon;

	// preferences
    private boolean prefMale;
	private boolean prefFemale;
	private boolean prefTrans;
	private int prefAgeMin;
	private int prefAgeMax;
	private int prefDistance;
	private int prefLocation;

	public boolean getIsMale(){
		return this.male && !this.female;
	}

	public boolean getIsFemale(){
		return female && !male;
	}

	public boolean getIsTrans(){
		return male && female;
	}

	public void setIsMale(){
		this.female = false;
		this.male = true;
	}

	public void setIsFemale(){
		this.female = true;
		this.male = false;
	}

	public void setIsTrans(){
		this.female = true;
		this.male = true;
	}

	public long getid() {
		return id;
	}

	public void setid(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLocation() {
		return location == null ? "" : location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

    public boolean getPrefMale() {
        return prefMale;
    }

    public void setPrefMale(boolean preferenceMale) {
        this.prefMale = preferenceMale;
    }

    public boolean getPrefFemale() {
        return prefFemale;
    }

    public void setPrefFemale(boolean prefFemale) {
        this.prefFemale = prefFemale;
    }

    public boolean getPrefTrans() {
        return prefTrans;
    }

    public void setPrefTrans(boolean prefTrans) {
        this.prefTrans = prefTrans;
    }

	public int getPrefAgeMin() {
		return prefAgeMin;
	}

	public void setPrefAgeMin(int prefAge) {
		this.prefAgeMin = prefAge;
	}

	public int getPrefAgeMax() {
		return prefAgeMax;
	}

	public void setPrefAgeMax(int prefAgeMax) {
		this.prefAgeMax = prefAgeMax;
	}

	public int getPrefDistance() {
		return prefDistance;
	}

	public void setPrefDistance(int prefDistance) {
		this.prefDistance = prefDistance;
	}

	public int getPrefLocation() {
		return prefLocation;
	}

	public void setPrefLocation(int prefLocation) {
		this.prefLocation = prefLocation;
	}

	public void setLat(long lat){
		this.lat = lat;
	}

	public void setLon(long lon){
		this.lon = lon;
	}

	public long getLat(){
		return this.lat;
	}

	public long getLon(){
		return this.lon;
	}

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
		return getLocation().equals(user.getLocation());

	}

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

	public User clone(){
		return new User(this.id, this.email, this.firstname, this.lastname, this.location, this.age,
		this.prefMale, this.prefFemale, this.prefTrans, this.prefAgeMin, this.prefAgeMax, this.prefDistance, this.prefLocation, this.male, this.female);
	}
}

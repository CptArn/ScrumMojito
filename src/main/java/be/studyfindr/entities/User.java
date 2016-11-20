package be.studyfindr.entities;

import org.bson.Document;

public class User {
	public User(long id, String email, String firstname, String lastname, String location, int age, boolean preferenceMale, boolean preferenceFemale, boolean preferenceTrans) {
		super();
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.location = location;
		this.age = age;
        this.preferenceMale = preferenceMale;
        this.preferenceFemale = preferenceFemale;
        this.preferenceTrans = preferenceTrans;
	}

	public User(Document doc) {
		this.id = doc.getLong("_id");
		this.email = doc.getString("email");
		this.firstname = doc.getString("firstname");
		this.lastname = doc.getString("lastname");
		this.location = doc.getString("location");
		this.age = doc.getInteger("age");
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", location=" + location + ", age=" + age + "]";
	}

	private long id;
	private String email;
	private String firstname;
	private String lastname;
	private String location;
	private int age;

	// Genders of interest
    private boolean preferenceMale;
	private boolean preferenceFemale;
	private boolean preferenceTrans;

	public long getid() {
		return id;
	}

	public void setid(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
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
		return location;
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

    public boolean getPreferenceMale() {
        return preferenceMale;
    }

    public void setPreferenceMale(boolean preferenceMale) {
        this.preferenceMale = preferenceMale;
    }

    public boolean getPreferenceFemale() {
        return preferenceFemale;
    }

    public void setPreferenceFemale(boolean preferenceFemale) {
        this.preferenceFemale = preferenceFemale;
    }

    public boolean getPreferenceTrans() {
        return preferenceTrans;
    }

    public void setPreferenceTrans(boolean preferenceTrans) {
        this.preferenceTrans = preferenceTrans;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (age != other.age)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}


}

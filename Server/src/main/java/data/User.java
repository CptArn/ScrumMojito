package data;

public class User {
    public User(int facebook_id, String email, String firstname, String lastname, String location, int age) {
	super();
	this.facebook_id = facebook_id;
	this.email = email;
	this.firstname = firstname;
	this.lastname = lastname;
	this.location = location;
	this.age = age;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", facebook_id=" + facebook_id + ", email=" + email + ", firstname=" + firstname
		+ ", lastname=" + lastname + ", location=" + location + ", age=" + age + "]";
    }

    private int id;
    private int facebook_id;
    private String email;
    private String firstname;
    private String lastname;
    private String location;
    private int age;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getFacebook_id() {
	return facebook_id;
    }

    public void setFacebook_id(int facebook_id) {
	this.facebook_id = facebook_id;
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

}

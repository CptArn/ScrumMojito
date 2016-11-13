package Entities;

public class Photo {
    public Photo(int id, int user_Id, String path) {
	super();
	this.id = id;
	this.user_Id = user_Id;
	this.path = path;
    }

    private int id;
    private int user_Id;
    private String path;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getUser_id() {
	return user_Id;
    }

    public void setUser_id(int user_id) {
	user_Id = user_id;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

}

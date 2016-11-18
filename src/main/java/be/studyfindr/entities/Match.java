package be.studyfindr.entities;

public class Match {
    public Match(int user1_Id, int user2_Id) {
	super();
	this.user1_Id = user1_Id;
	this.user2_Id = user2_Id;
    }

    private int user1_Id;
    private int user2_Id;

    public int getUser1_Id() {
	return user1_Id;
    }

    public void setUser1_Id(int user1_Id) {
	this.user1_Id = user1_Id;
    }

    public int getUser2_Id() {
	return user2_Id;
    }

    public void setUser2_Id(int user2_Id) {
	this.user2_Id = user2_Id;
    }
}

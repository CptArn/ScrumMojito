package be.studyfindr.entities;

import org.bson.Document;

public class Match {
    public Match(long liker_Id, long likee_Id) {
        super();
        this.liker_Id = liker_Id;
        this.likee_Id = likee_Id;
    }

    private long liker_Id;
    private long likee_Id;

    public long getLiker_Id() {
        return liker_Id;
    }

    public void setLiker_Id(long liker_Id) {
        this.liker_Id = liker_Id;
    }

    public long getLikee_Id() {
        return likee_Id;
    }

    public void setLikee_Id(long likee_id) {
        this.likee_Id = likee_id;
    }

    public boolean checkIfMatch(Like liker, Like likee) {
        if (liker.getLike() && likee.getLike() && liker.getStatus() && likee.getStatus()) {
            return true;
        }
        return false;
    }
}

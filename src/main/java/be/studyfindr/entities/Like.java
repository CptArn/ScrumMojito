package be.studyfindr.entities;

import org.bson.Document;

public class Like {
    /**
     * Create a new like
     * @param user1_Id id of the liker
     * @param user2_Id id of the likee
     */
    public Like(long user1_Id, long user2_Id, boolean like, boolean confirmed) {
        super();
        this.liker_id = user1_Id;
        this.likee_id = user2_Id;
        this.like = like;
        this.confirmed = confirmed;
    }

    public Like(Document doc) {
        super();
        if (doc == null) throw new IllegalArgumentException("doc cannot be null");
        this.liker_id = doc.getLong("liker_id");
        this.likee_id = doc.getLong("likee_id");
        this.like = doc.getBoolean("like");
        this.confirmed = doc.getBoolean("confirmed");
    }

    private long liker_id;
    private long likee_id;
    private boolean like;
    private boolean confirmed;

    public long getLiker_Id() {
        return liker_id;
    }

    public long getLikee_Id() {
        return likee_id;
    }

    public boolean getLike() {
        return like;
    }

    public boolean getStatus() {
        return confirmed;
    }

    public void setStatus(boolean status) {
        this.confirmed = status;
    }

}

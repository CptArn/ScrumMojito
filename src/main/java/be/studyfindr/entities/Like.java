package be.studyfindr.entities;

import org.bson.Document;

public class Like {
    /**
     * Create a new like
     * @param liker_Id id of the liker
     * @param likee_id id of the likee
     */
    public Like(long liker_Id, long likee_id, boolean like, boolean confirmed) {
        super();
        this.liker_id = liker_Id;
        this.likee_id = likee_id;
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

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean getStatus() {
        return confirmed;
    }

    public void setStatus(boolean status) {
        this.confirmed = status;
    }

}

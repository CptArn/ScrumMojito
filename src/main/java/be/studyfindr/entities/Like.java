package be.studyfindr.entities;

import org.bson.Document;

/**
 * Represents a like
 */
public class Like {
    /**
     * Create a new like
     * @param liker_Id id of the liker
     * @param likee_id id of the likee
     */
    public Like(long liker_Id, long likee_id, boolean like) {
        super();
        this.liker_id = liker_Id;
        this.likee_id = likee_id;
        this.like = like;
    }

    /**
     * Parse a like from document
     * @param doc document to parse
     */
    public Like(Document doc) {
        super();
        if (doc == null) throw new IllegalArgumentException("doc cannot be null");
        this.liker_id = doc.getLong("liker_id");
        this.likee_id = doc.getLong("likee_id");
        this.like = doc.getBoolean("like");
    }

    // privates
    private long liker_id;
    private long likee_id;
    private boolean like;

    /**
     * Returns the liker id
     * @return liker id
     */
    public long getLiker_Id() {
        return liker_id;
    }

    /**
     * Returns the likee id
     * @return likee id
     */
    public long getLikee_Id() {
        return likee_id;
    }

    /**
     * Returns the like status
     * @return like status, true=like / false=dislike
     */
    public boolean getLike() {
        return like;
    }

    /**
     * Sets the like status
     * @param like like status, true=like / false=dislike
     */
    public void setLike(boolean like) {
        this.like = like;
    }
}

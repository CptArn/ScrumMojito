package be.studyfindr.entities;

import org.bson.Document;

public class Like {
    public Like(long user1_Id, long user2_Id) {
        super();
        this.liker_id = user1_Id;
        this.likee_id = user2_Id;

    }

    public Like(Document doc) {
        super();
        if (doc != null) {
            this.liker_id = doc.getLong("liker_id");
            this.likee_id = doc.getLong("likee_id");
        }

    }

    private long liker_id;
    private long likee_id;

    public long getLiker_Id() {
        return liker_id;
    }

    public long getLikee_Id() {
        return likee_id;
    }

}

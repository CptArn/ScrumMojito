package be.studyfindr.entities;

import org.bson.Document;

import java.util.Date;

/**
 * Represents a message
 */
public class Message {

    /**
     * Creates a new message instance
     * @param message message content
     * @param date moment the message was received
     * @param sender_Id sender id
     * @param receiver_Id receiver id
     */
    public Message(String message, Date date, long sender_Id, long receiver_Id) {
        super();
        this.message = message;
        this.date = date;
        this.sender_Id = sender_Id;
        this.receiver_Id = receiver_Id;
    }

    /**
     * Parses a doc and creates a new message object
     * @param doc document to parse
     * @throws ClassCastException if parsing fails
     */
    public Message(Document doc) throws ClassCastException {
        if (doc == null) throw new IllegalArgumentException("The document cannot be 'null'.");
        String idFieldName;
        if (doc.containsKey("_id")){
            idFieldName = "_id";
        }else if (doc.containsKey("id")){
            idFieldName = "id";
        }else{
            throw new IllegalArgumentException("Cannot find the id.");
        }
        try{
            this.id = (long)doc.getInteger(idFieldName);
        }catch(Exception ex){
            this.id = doc.getLong(idFieldName);
        }
        this.message = doc.getString("message");
        try {
            this.date = doc.getDate("date");
        }catch(Exception ex){
            this.date = new Date(doc.getLong("date"));
        }
        try {
            this.sender_Id = doc.getLong("sender_Id");
            this.receiver_Id = doc.getLong("receiver_Id");
        }catch(Exception ex){
            this.sender_Id = (long)doc.getInteger("sender_Id");
            this.receiver_Id = (long)doc.getInteger("receiver_Id");
        }
    }

    // privates
    private long id;
    private String message;
    private Date date;
    private long sender_Id;
    private long receiver_Id;

    /**
     * Gets the id
     * @return new id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id
     * @param id new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message
     * @param message new message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the date
     * @return date of the message
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date
     * @param date new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the sender id
     * @return sender id
     */
    public long getSender_Id() {
        return sender_Id;
    }

    /**
     * Sets the sender id
     * @param sender_Id new sender id
     */
    public void setSender_Id(long sender_Id) {
        this.sender_Id = sender_Id;
    }

    /**
     *Gets the receiver id
     * @return receiver id
     */
    public long getReceiver_Id() {
        return receiver_Id;
    }

    /**
     * Sets the receiver id
     * @param receiver_Id new receiver id
     */
    public void setReceiver_Id(long receiver_Id) {
        this.receiver_Id = receiver_Id;
    }

    /**
     * Compares the current Message with an other Message or object.
     * @param obj other Message or object
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (sender_Id != other.sender_Id) return false;
        return receiver_Id == other.receiver_Id;
    }
}

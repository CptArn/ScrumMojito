package be.studyfindr.entities;

import org.bson.Document;

import java.util.Date;

public class Message {
    public Message(long id, String message, Date date, boolean status, int sender_Id, int receiver_Id) {
        super();
        this.id = id;
        this.message = message;
        this.date = date;
        this.status = status;
        this.sender_Id = sender_Id;
        this.receiver_Id = receiver_Id;
    }

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
        this.id = doc.getLong(idFieldName);
        this.message = doc.getString("message");
        this.date = doc.getDate("date");
        this.status = doc.getBoolean("status", false);
        this.sender_Id = doc.getInteger("sender_Id", -1);
        this.receiver_Id = doc.getInteger("receiver_Id", -1);
    }

    private long id;
    private String message;
    private Date date;
    private boolean status;
    private int sender_Id;
    private int receiver_Id;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSender_Id() {
        return sender_Id;
    }

    public void setSender_Id(int sender_Id) {
        this.sender_Id = sender_Id;
    }

    public int getReceiver_Id() {
        return receiver_Id;
    }

    public void setReceiver_Id(int receiver_Id) {
        this.receiver_Id = receiver_Id;
    }

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
        if (id != other.id)
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (sender_Id != other.sender_Id) return false;
        if (receiver_Id != other.receiver_Id) return false;
        return status == other.status;
    }
}

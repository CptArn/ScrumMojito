package be.studyfindr.entities;

import java.util.Date;

public class Message {
    public Message(int id, String message, Date date, boolean status, int sender_Id, int receiver_Id) {
        super();
        this.id = id;
        this.message = message;
        this.date = date;
        this.status = status;
        this.sender_Id = sender_Id;
        this.receiver_Id = receiver_Id;
    }

    private int id;
    private String message;
    private Date date;
    private boolean status;
    private int sender_Id;
    private int receiver_Id;

    public int getId() {
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

}

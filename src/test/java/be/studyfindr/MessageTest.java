package be.studyfindr;

import be.studyfindr.entities.Like;
import be.studyfindr.entities.Message;
import org.junit.Test;

import java.util.Date;

/**
 * Created by anthony on 27/11/2016.
 */
public class MessageTest {
    @Test
    public void test01TestSetters() {
        Message m1 = new Message("message", new Date(), 1, 2);
        Message m2 = new Message("-------", new Date(), 99, 89);
        m2.setDate(m1.getDate());
        m2.setId(m1.getId());
        m2.setMessage(m1.getMessage());
        m2.setReceiver_Id(m1.getReceiver_Id());
        m2.setSender_Id(m1.getSender_Id());
        assert(m1.equals(m2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test02DocumentParseFail(){
        Message m = new Message(new org.bson.Document());
    }

    @Test
    public void test03Equals(){
        Message message1 = new Message("", new Date(), 1, 2);
        Message message2 = new Message(null, new Date(), 1, 2);
        Message message3 = new Message("", new Date(46446), 1, 2);
        Message message4 = new Message("", new Date(), 3, 2);
        Message message5 = new Message("", new Date(), 1, 3);
        Message message6 = new Message("", null, 1, 3);
        Object obj = new Object();
        assert(message1.equals(null) == false &&
                message1.equals(message1) == true &&
                message1.equals(message2) == false &&
                message1.equals(message3) == false &&
                message1.equals(message4) == false &&
                message1.equals(message5) == false &&
                message1.equals(obj) == false &&
                message2.equals(message1) == false &&
                message6.equals(message5) == false
        );
    }
}

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
    public void test1TestSetters() {
        Message m1 = new Message(1, "message", new Date(), false, 1, 2);
        Message m2 = new Message(2, "-------", new Date(), true, 99, 89);
        m2.setDate(m1.getDate());
        m2.setId(m1.getId());
        m2.setMessage(m1.getMessage());
        m2.setReceiver_Id(m1.getReceiver_Id());
        m2.setSender_Id(m1.getSender_Id());
        m2.setStatus(m1.getStatus());
        assert(m1.equals(m2));
    }
}

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
        Message m1 = new Message("message", new Date(), 1, 2);
        Message m2 = new Message("-------", new Date(), 99, 89);
        m2.setDate(m1.getDate());
        m2.setId(m1.getId());
        m2.setMessage(m1.getMessage());
        m2.setReceiver_Id(m1.getReceiver_Id());
        m2.setSender_Id(m1.getSender_Id());
        assert(m1.equals(m2));
    }
}

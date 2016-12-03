package be.studyfindr;

import org.junit.Test;
import be.studyfindr.rest.Util;

/**
 * Created by anthony on 29/11/2016.
 */
public class UtilTest {

    @Test
    public void test1Haversine(){
        assert(Math.abs(Util.computeDistance(41.8350, 12.470, 41.9133741000, 12.5203944000) - 9.66) < 0.005);
    }
}

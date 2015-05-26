package date;

import org.testng.annotations.Test;

import java.util.Calendar;

/**
 * User: YamStranger
 * Date: 5/26/15
 * Time: 8:12 PM
 */
public class DatesTest {

    @Test
    public void difference_test_proceed() throws InterruptedException {
        Dates current = new Dates();
        Thread.sleep(1000);
        Dates now = new Dates();
        long dif = now.difference(current, Calendar.SECOND);
        System.out.println(dif);
    }
}

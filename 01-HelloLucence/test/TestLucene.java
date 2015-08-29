import org.itat.test.HelloLucene;
import org.junit.After;
import org.junit.Test;

/**
 * Author: Íõ¿¡³¬
 * Date: 2015-08-29
 * Time: 09:29
 * Declaration: All Rights Reserved !!!
 */
public class TestLucene {

    @Test
    public void testIndex() {
        HelloLucene hl = new HelloLucene();
        hl.index();
    }

    @Test
    public void testSearch() {
        HelloLucene hl = new HelloLucene();
        hl.search();
    }
}

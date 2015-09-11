package org.itat.lucene.test;

import org.ita.lucene.util.CustomParserUtil;
import org.junit.Test;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 15:44
 * Declaration: All Rights Reserved !!!
 */
public class TestCustomParser {
    @Test
    public void test01() {
        CustomParserUtil cpu = new CustomParserUtil();
//        cpu.searcherByCustomQuery("java");
        // 不能使用模糊查询
//        cpu.searcherByCustomQuery("java~");
        // 不能使用通配符查询
//        cpu.searcherByCustomQuery("ja?va");

//        cpu.searcherByCustomQuery("size:[0 TO 4096]");
//        cpu.searcherByCustomQuery("content:[a TO j]");

        cpu.searcherByCustomQuery("date:[2015-08-17 TO 2015-08-29]");
    }
}

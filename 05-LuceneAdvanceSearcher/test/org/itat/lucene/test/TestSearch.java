package org.itat.lucene.test;

import org.ita.lucene.util.SearchTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: 王俊超
 * Date: 2015-09-01
 * Time: 11:05
 * Declaration: All Rights Reserved !!!
 */
public class TestSearch {
    private SearchTest st;

    @Before
    public void init() {
        st = new SearchTest();
    }

    @Test
    public void test01() {
        st.searcher("java", null);
    }
}

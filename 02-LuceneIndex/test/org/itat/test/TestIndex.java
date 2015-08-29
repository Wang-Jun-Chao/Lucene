package org.itat.test;

import org.itat.index.IndexUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Íõ¿¡³¬
 * Date: 2015-08-29
 * Time: 15:19
 * Declaration: All Rights Reserved !!!
 */
public class TestIndex {

    private IndexUtil iu;
    @Before
    public void before() {
        iu = new IndexUtil();
    }

    @Test
    public void testIndex() {
        iu.index();
    }

    @Test
    public void testQuery() {
        iu.query();
    }

    @Test
    public void testDelete() {
        iu.delete();
    }

    @Test
    public void testUndelete() {
        iu.undelete();
    }

    @Test
    public void testForceDelete() {
        iu.forceDelete();
    }

    @Test
    public void testMerge() {
        iu.merge();
    }

    @Test
    public void testUpdate() {
        iu.update();
    }

    @Test
    public void testSearch() {
         iu.search();
    }
}

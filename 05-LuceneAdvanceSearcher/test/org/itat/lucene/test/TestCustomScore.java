package org.itat.lucene.test;

import org.ita.lucene.util.MyScoreQuery;
import org.junit.Test;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 10:05
 * Declaration: All Rights Reserved !!!
 */
public class TestCustomScore {
    @Test
    public void test01() {
        MyScoreQuery msQuery = new MyScoreQuery();
        msQuery.searchByScoreQuery();
    }

    @Test
    public void test02() {
        MyScoreQuery msQuery = new MyScoreQuery();
        msQuery.searchByFileScoreQuery();
    }

    @Test
    public void test03() {
        MyScoreQuery msQuery = new MyScoreQuery();
        msQuery.searchByDateScoreQuery();
    }
}

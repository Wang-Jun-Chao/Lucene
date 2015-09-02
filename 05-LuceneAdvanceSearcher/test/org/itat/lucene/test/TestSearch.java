package org.itat.lucene.test;

import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.ita.lucene.util.FileIndexUtils;
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
    public void index() {
        FileIndexUtils.index(true);
    }

    @Test
    public void test01() {
        // 按评分进行排序
        st.searcherBySort("java", null);
        // 通过doc的id进行排序
//        st.searcherBySort("java", Sort.INDEXORDER);
        // 使用默认的评分进行排序
//        st.searcherBySort("java", Sort.RELEVANCE);
        // 通过文件大小排序
//        st.searcherBySort("java", new Sort(new SortField("size", SortField.Type.LONG, true)));
//        st.searcherBySort("java", new Sort(new SortField("size", SortField.Type.LONG, false)));

        // 通过日期排序
//        st.searcherBySort("java", new Sort(new SortField("date", SortField.Type.LONG, false)));

        // TODO 文件名进行排序还有问题
//        st.searcherBySort("java", new Sort(new SortField("filename", SortField.Type.LONG, false)));

        // 多个条件
//        st.searcherBySort("java", new Sort(new SortField("size", SortField.Type.LONG, false),SortField.FIELD_SCORE));
    }

    @Test
    public void test02() {
        // TermRangeFilter 已经不建议使用了
        Filter trf = TermRangeFilter.newStringRange("filename", "Ant.he", "Ant.txt", true, true);
//        TermRangeQuery trq = new TermRangeQuery("name", new BytesRef("a".getBytes()),
//                new BytesRef("f".getBytes()), true, true);

        st.searcherByFilter("Ant", trf);
    }
}

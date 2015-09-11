package org.itat.lucene.test;

import org.apache.lucene.index.Term;
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
//        st.searcherBySort("java", null);
        // 通过doc的id进行排序
        st.searcherBySort("java", Sort.INDEXORDER);
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
        // NOTE TermRangeFilter 已经不建议使用了,使用TermRangeQuery
//        Filter trf = TermRangeFilter.newStringRange("filename", "Ant.he", "Ant.txt", true, true);
//        st.searcherByFilter("Ant.he", trf);
        // NOTE NumericRangeFilter 已经不建议使用了，使用NumericRangeQuery
//        Filter filter = NumericRangeFilter.newLongRange("filename", 0L, 4096L, true, true);

        Filter filter = new QueryWrapperFilter(new WildcardQuery(new Term("filename", "*.txt")));

        st.searcherByFilter(filter);

//        Query query = new TermRangeQuery("filename", new BytesRef("Abdera.he".getBytes()),
//                new BytesRef("Ant.txt".getBytes()), true, true);
//
//        query = NumericRangeQuery.newLongRange("size", 0L, 4096L, true, true);
//
//        st.searcherByQuery(query);
    }

    @Test
    public void test03() {
        Query query = new WildcardQuery(new Term("filename", "*.txt"));
        st.searcherByQuery(query);
    }
}

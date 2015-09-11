package itat.org.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.itat.index.FileIndexUtils;
import org.itat.index.SearcherUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Author: 王俊超
 * Date: 2015-08-30
 * Time: 13:50
 * Declaration: All Rights Reserved !!!
 */
public class TestSearch {
    private SearcherUtil su;

    @Before
    public void init() {
        su = new SearcherUtil();
    }

    @Test
    public void testCopyFile() {
        File dir = new File("d:/com.action.lucene/example/");
        try {
            for (File file : dir.listFiles()) {
                String destFileName = FilenameUtils.getFullPath(file.getAbsolutePath()) +
                        FilenameUtils.getBaseName(file.getName()) + ".he";
                FileUtils.copyFile(file, new File(destFileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchByTerm() {
//        su.searchByTerm("name", "mike", 3);
        su.searchByTerm("content", "i", 3);
    }

    @Test
    public void testSearchByTermRange() {
//        su.searchByTermByRange("id", "1", "3", 10);
        // 查询以a-s开头的名字
//        su.searchByTermByRange("name", "a", "s", 10);
        // attach是以数字开头所以查不出来
        su.searchByTermByRange("attach", "1", "10", 10);
    }

    @Test
    public void testSearchByNumericRange() {
        su.searchNumericRange("attach", 1, 10, 10);
    }

    @Test
    public void testSearchByPrefix() {
//        su.searchByPrefix("name", "j", 10);
        su.searchByPrefix("content", "s", 10);
    }

    @Test
    public void testSearchByWildcard() {
//        su.searchByWildcard("name", "j*", 10);
        // 匹配j开头的有三个字符的name
        su.searchByWildcard("email", "*@itat.org", 10);
        // 匹配@itat.org结尾的所有字符
        su.searchByWildcard("name", "j???", 10);
    }

    @Test
    public void testSearchByBoolean() {
        su.searchByBoolean(10);
    }

    @Test
    public void testSearchByPhrase() {
        su.searchByPrase(10);
    }

    @Test
    public void testSearchByFuzzy() {
        su.searchByFuzzy(10);
    }

    @Test
    public void testSearchByQueryParser() throws ParseException {
        // 1、创建QueryParser对象
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        // 设置默认操作
//        parser.setDefaultOperator(QueryParser.Operator.AND);
        // 搜索content中包含有like的
//        Query query = parser.parse("like")  ;
//        Query query = parser.parse("like football")  ;
//        Query query = parser.parse("I AND football")  ;

        // 默认是或
//        Query query = parser.parse("basketball football")  ;

        // 改变搜索域
//        Query query = parser.parse("name:mike");
//        Query query = parser.parse("name:j*");

        // 允许第一个字符中通配符，默认关闭，效率比较低
//        parser.setAllowLeadingWildcard(true);
//        Query query = parser.parse("email:*@itat.org");


        // 匹配name中没有mike但是content中必须有football的，+和-要放置到域说明前面
//        Query query = parser.parse("- name:mike + football");

        // 匹配一个区间，TO必须大写
//        Query query = parser.parse("id:[1 TO 3]");
//        Query query = parser.parse("id:{1 TO 3}");

        // 完全匹配I Like Football的
//        Query query = parser.parse("\"I like football\"");

//        Query query = parser.parse("\"I like football\"");

        // 匹配 I 和 football 之间有一个单词距离的
//        Query query = parser.parse("\"I like football\"~1");

        // 模糊查询
//        Query query = parser.parse("name:make~");

        //没有办法匹配数字范围（自己扩展Parser）
        Query query = parser.parse("attach:[2 TO 10]");

        su.searchByQueryParser(query, 10);
    }


    @Test
    public void indexFile() {
        FileIndexUtils.index(true);
    }

    @Test
    public void testSearchPage01() {
        su.searchPage("java", 1, 20);
        System.out.println("----------------");
//        su.searchNoPage("java");
        su.searchPageByAfter("java", 1, 20);
    }

    @Test
    public void testSearchPage02() {
        su.searchPageByAfter("java", 4, 20);
    }
}

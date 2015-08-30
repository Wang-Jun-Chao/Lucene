package org.itat.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 王俊超
 * Date: 2015-08-30
 * Time: 10:14
 * Declaration: All Rights Reserved !!!
 */
public class SearcherUtil {
    private Directory directory;
    private IndexReader reader;

    private String[] ids = {"1", "2", "3", "4", "5", "6"};
    private String[] emails = {"aa@itat.org", "bb@itat.org", "cc@cc.org", "dd@sina.org", "ee@zttc.edu", "ff@itat.org"};
    private String[] contents = {
            "welcome to visited the space,I like book",
            "hello boy, I like pingpeng ball",
            "my name is cc I like game",
            "I like football",
            "I like football and I like basketball too",
            "I like movie and swim"
    };
    private Date[] dates = null;
    private int[] attachs = {2, 3, 1, 4, 5, 5};
    private String[] names = {"zhangsan", "lisi", "john", "jetty", "mike", "jake"};
    private Map<String, Float> scores = new HashMap<String, Float>();


    public SearcherUtil() {
        directory = new RAMDirectory();
        setDates();
        index();
    }

    private void setDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dates = new Date[ids.length];
            dates[0] = sdf.parse("2010-02-19");
            dates[1] = sdf.parse("2012-01-11");
            dates[2] = sdf.parse("2011-09-19");
            dates[3] = sdf.parse("2010-12-22");
            dates[4] = sdf.parse("2012-01-01");
            dates[5] = sdf.parse("2011-05-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public IndexSearcher getSearcher() {
        try {
            if (reader == null) {
                reader = DirectoryReader.open(directory);

            } else {
                reader = DirectoryReader.openIfChanged((DirectoryReader) reader);
            }

            if (reader == null) {
                reader = DirectoryReader.open(directory);
            }
            return new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void index() {

        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            writer.deleteAll();
            Document doc = null;
            for (int i = 0; i < ids.length; i++) {

                // 不使用评分
//                doc = new Document();
//                String et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
//                System.out.println(et);
//                StringField idField = new StringField("id", ids[i], Field.Store.YES);
//                doc.add(idField);
//                TextField emailField = new TextField("email", emails[i], Field.Store.YES);
//                doc.add(emailField);
//                TextField contentFiled = new TextField("content", contents[i], Field.Store.YES);
//                doc.add(contentFiled);
//                StringField nameFiled = new StringField("name", names[i], Field.Store.YES);
//                doc.add(nameFiled);

                // 使用评分
                String et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
                doc = new Document();

                StringField idField = new StringField("id", ids[i], Field.Store.YES);
                doc.add(idField);

                StringField emailField = new StringField("email", emails[i], Field.Store.YES);
                doc.add(emailField);

                TextField contentFiled = new TextField("content", contents[i], Field.Store.YES);
                doc.add(contentFiled);

                StringField nameFiled = new StringField("name", names[i], Field.Store.YES);
                doc.add(nameFiled);

                // 存储数字
                IntField attachesField = new IntField("attach", attachs[i], Field.Store.YES);
                doc.add(attachesField);

                // 存储日期
                LongField dateField = new LongField("date", dates[i].getTime(), Field.Store.YES);
                doc.add(dateField);

                if (scores.containsKey(et)) {
                    contentFiled.setBoost(scores.get(et));
                } else {
                    contentFiled.setBoost(0.5F);
                }

                writer.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchByTerm(String field, String name, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            Query query = new TermQuery(new Term(field, name));
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + sd.doc + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("id") + ","
                        + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByTermByRange(String field, String start, String end, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            Query query = new TermRangeQuery(field, new BytesRef(start), new BytesRef(end), true, true);
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchNumericRange(String field, int start, int end, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            Query query = NumericRangeQuery.newIntRange(field, start, end, true, true);
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByPrefix(String field, String value, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            Query query = new PrefixQuery(new Term(field, value));
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByWildcard(String field, String value, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            // 在传入的value中可以使用通配符:?和*,?表示匹配一个字符，*表示匹配任意多个字符
            Query query = new WildcardQuery(new Term(field, value));
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByBoolean(int num) {
        try {
            IndexSearcher searcher = getSearcher();
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new TermQuery(new Term("name", "zhangsan")), BooleanClause.Occur.MUST_NOT)
//            builder.add(new TermQuery(new Term("name", "zhangsan")), BooleanClause.Occur.MUST)
//                    .add(new TermQuery(new Term("content", "like")), BooleanClause.Occur.MUST);
//                    .add(new TermQuery(new Term("content", "game")), BooleanClause.Occur.MUST);
                    .add(new TermQuery(new Term("content", "game")), BooleanClause.Occur.SHOULD);
            BooleanQuery query = builder.build();

            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByPrase(int num) {
        try {
            IndexSearcher searcher = getSearcher();
            PhraseQuery.Builder builder = new PhraseQuery.Builder();
            // 设置距离
            builder.setSlop(3);
//            // 第一个Term
//            builder.add(new Term("content", "i"));
//            // 第二个Term
//            builder.add(new Term("content", "football"));
            // 第一个Term
            builder.add(new Term("content", "pingpeng"));
            // 第二个Term
            builder.add(new Term("content", "i"));
            Query query = builder.build();
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByFuzzy(int num) {
        try {
            IndexSearcher searcher = getSearcher();
//            Query query = new FuzzyQuery(new Term("name", "mike"));
            Query query = new FuzzyQuery(new Term("name", "make"));
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchByQueryParser(Query query, int num) {
        try {
            IndexSearcher searcher = getSearcher();
            TopDocs tds = searcher.search(query, num);
            System.out.println("一共查询了：" + tds.totalHits);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + doc.get("id") + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

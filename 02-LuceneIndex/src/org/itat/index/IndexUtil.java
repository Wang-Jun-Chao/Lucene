package org.itat.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 王俊超
 * Date: 2015-08-29
 * Time: 14:48
 * Declaration: All Rights Reserved !!!
 */
public class IndexUtil {
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
    private Directory directory = null;
    private Map<String, Float> scores = new HashMap<String, Float>();
    private static IndexReader reader = null;


    public IndexUtil() {
        try {
            setDates();
            scores.put("itat.org", 2.0F);
            scores.put("zttc.edu", 1.5F);
            directory = FSDirectory.open(Paths.get("d:/com.action.lucene/index02"));
            // 存储在内存中使用时要与index()方法一起使用
//            directory = new RAMDirectory();
//            index();

            reader = DirectoryReader.open(directory);
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public void update() {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {

            // Lucene并没有提供更新，这里的更新操作其实是如下两个操作的合集
            // 先删除之后再添加
            Document doc = new Document();
            doc.add(new StringField("id", "11", Field.Store.YES));
            doc.add(new TextField("email", emails[0], Field.Store.YES));
            doc.add(new TextField("content", contents[0], Field.Store.YES));
            doc.add(new StringField("name", names[0], Field.Store.YES));

            writer.updateDocument(new Term("id", "1"), doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void merge() {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            // 分将索引合并成两段，删除后的数据会被清空
            // 3.5后不建议使用，由Lucene自动处理
            writer.forceMerge(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forceDelete() {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            writer.forceMergeDeletes();
            writer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void undelete() {

        try (IndexReader reader = DirectoryReader.open(directory)) {
            // 3.6后不能使用reader进行还原了
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // 使用Writer进行恢复。未能实现
//        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
//            writer.rollback();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void delete() {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            // 参数是一个选项，可以是一个Query，也可以是一个Term（精确值）
            writer.deleteDocuments(new Term("id", "1"));
            writer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete02() {
        // 使用reader进行删除，但是5.3已经不能使用
//            reader.deleteDocuments(new Term("id", "1"));
    }

    public void query() {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            // 获取文档数量
            System.out.println("numDocs: " + reader.numDocs());
            System.out.println("maxDocs: " + reader.maxDoc());
            System.out.println("deleteDocs: " + reader.numDeletedDocs());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                System.out.println(et);
                doc = new Document();
                if (scores.containsKey(et)) {
                    StringField idField = new StringField("id", ids[i], Field.Store.YES);
                    doc.add(idField);

                    TextField emailField = new TextField("email", emails[i], Field.Store.YES);
                    emailField.setBoost(scores.get(et));
                    doc.add(emailField);

                    TextField contentFiled = new TextField("content", contents[i], Field.Store.YES);
                    contentFiled.setBoost(scores.get(et));
                    doc.add(contentFiled);

                    StringField nameFiled = new StringField("name", names[i], Field.Store.YES);
                    doc.add(nameFiled);

                    // 存储数字
                    IntField attachesField = new IntField("attach", attachs[i], Field.Store.YES);
                    doc.add(attachesField);

                    // 存储日期
                    LongField dateField = new LongField("date", dates[i].getTime(), Field.Store.YES);
                    doc.add(dateField);

                } else {
                    StringField idField = new StringField("id", ids[i], Field.Store.YES);
                    doc.add(idField);

                    TextField emailField = new TextField("email", emails[i], Field.Store.YES);
                    emailField.setBoost(0.5F);
                    doc.add(emailField);

                    TextField contentFiled = new TextField("content", contents[i], Field.Store.YES);
                    contentFiled.setBoost(0.5F);
                    doc.add(contentFiled);

                    StringField nameFiled = new StringField("name", names[i], Field.Store.YES);
                    doc.add(nameFiled);

                    // 存储数字
                    IntField attachesField = new IntField("attach", attachs[i], Field.Store.YES);
                    doc.add(attachesField);

                    // 存储日期
                    LongField dateField = new LongField("date", dates[i].getTime(), Field.Store.YES);
                    doc.add(dateField);
                }


                writer.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search01() {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TermQuery query = new TermQuery(new Term("content", "like"));
            TopDocs tds = searcher.search(query, 10);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("(" + sd.doc + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("id") + ","
                        + doc.get("attach") + "," + doc.get("date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search02() {
        try {
            IndexSearcher searcher = getSearcher();
            TermQuery query = new TermQuery(new Term("content", "like"));
            TopDocs tds = searcher.search(query, 10);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
//                System.out.println("(" + sd.doc + "-" + doc.getField("content").boost() + "-" + sd.score + ")"
//                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("id") + ","
//                        + doc.get("attach") + "," + doc.get("date"));
                System.out.println("(" + doc.get("id") + ")"
                        + doc.get("name") + "[" + doc.get("email") + "]-->" + doc.get("id") + ","
                        + doc.get("attach") + "," + doc.get("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
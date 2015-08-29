package org.itat.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
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
            directory = FSDirectory.open(Paths.get("d:/lucene/index02"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void merge() {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            // 分将索引合并成两段，删除后的数据会被清空
            // 3.5后不建议使用，由Lucene自动处理
            writer.forceMerge(2);
            writer.commit();
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

//        // 使用Writer进行恢复
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
            Document doc = null;
            for (int i = 0; i < ids.length; i++) {
                doc = new Document();
                doc.add(new StringField("id", ids[i], Field.Store.YES));
                doc.add(new TextField("email", emails[i], Field.Store.YES));
                doc.add(new TextField("content", contents[i], Field.Store.YES));
                doc.add(new StringField("name", names[i], Field.Store.YES));

                writer.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

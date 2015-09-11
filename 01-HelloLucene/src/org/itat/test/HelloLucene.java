package org.itat.test;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Author: 王俊超
 * Date: 2015-08-29
 * Time: 08:50
 * Declaration: All Rights Reserved !!!
 */
public class HelloLucene {
    /**
     * 建立索引
     */
    public void index() {

        IndexWriter writer = null;
        try {
            // 1、创建Directory
            // 建立在内存中
//            Directory directory = new RAMDirectory();
            // 建立在硬盘上
            Directory directory = FSDirectory.open(Paths.get("d:/com.action.lucene/index01"));

            // 2、创建IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
            writer = new IndexWriter(directory, iwc);


            // 3、创建Document
            Document doc = null;

            // 4、为Document添加Field
            File f = new File("D:/com.action.lucene/example");

            for (File file : f.listFiles()) {

                System.out.println(file);
                String str = FileUtils.readFileToString(file);
                System.out.println(str);

                doc = new Document();
                doc.add(new TextField("content", new FileReader(file)));
                doc.add(new StringField("filename", file.getName(), Field.Store.YES));
                doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));

                // 5、通过IndexWriter添加文档
                writer.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索
     */

    public void search() {

        try {
            // 1、创建Directory
            // 建立在硬盘上
            Directory directory = FSDirectory.open(Paths.get("d:/com.action.lucene/index01"));

            // 2、创建IndexReader
            IndexReader reader = DirectoryReader.open(directory);

            // 3、根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4、创建搜索的Query
            // 要搜索文件的内容
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            //搜索文档中包含java的文档
            Query query = parser.parse("java");

            // 5、根据seacher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7、根据seacher和ScoreDoc对象获取具体的Document对象
                Document d = searcher.doc(sd.doc);

                // 8、根据Document对象获取需要的值
                System.out.println(d.get("filename") + "[" + d.get("path") + "]");
                System.out.println(d.get("filename") + "[" + d.get("content") + "]");
            }

            // 9、关闭reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

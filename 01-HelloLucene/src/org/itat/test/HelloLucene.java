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
 * Author: ������
 * Date: 2015-08-29
 * Time: 08:50
 * Declaration: All Rights Reserved !!!
 */
public class HelloLucene {
    /**
     * ��������
     */
    public void index() {

        IndexWriter writer = null;
        try {
            // 1������Directory
            // �������ڴ���
//            Directory directory = new RAMDirectory();
            // ������Ӳ����
            Directory directory = FSDirectory.open(Paths.get("d:/lucene/index01"));

            // 2������IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
            writer = new IndexWriter(directory, iwc);


            // 3������Document
            Document doc = null;

            // 4��ΪDocument���Field
            File f = new File("D:/lucene/example");

            for (File file : f.listFiles()) {

                System.out.println(file);
                String str = FileUtils.readFileToString(file);
                System.out.println(str);

                doc = new Document();
                doc.add(new TextField("content", new FileReader(file)));
                doc.add(new StringField("filename", file.getName(), Field.Store.YES));
                doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));

                // 5��ͨ��IndexWriter����ĵ�
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
     * ����
     */

    public void search() {

        try {
            // 1������Directory
            // ������Ӳ����
            Directory directory = FSDirectory.open(Paths.get("d:/lucene/index01"));

            // 2������IndexReader
            IndexReader reader = DirectoryReader.open(directory);

            // 3������IndexReader����IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4������������Query
            // Ҫ�����ļ�������
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            //�����ĵ��а���java���ĵ�
            Query query = parser.parse("java");

            // 5������seacher�������ҷ���TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6������TopDocs��ȡScoreDoc����
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7������seacher��ScoreDoc�����ȡ�����Document����
                Document d = searcher.doc(sd.doc);

                // 8������Document�����ȡ��Ҫ��ֵ
                System.out.println(d.get("filename") + "[" + d.get("path") + "]");
                System.out.println(d.get("filename") + "[" + d.get("content") + "]");
            }

            // 9���ر�reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

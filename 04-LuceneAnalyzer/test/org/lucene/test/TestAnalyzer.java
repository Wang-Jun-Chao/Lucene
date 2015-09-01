package org.lucene.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.itat.lucene.util.AnalyzerUtils;
import org.itat.lucene.util.MySameAnalyzer;
import org.itat.lucene.util.MyStopAnalyzer;
import org.itat.lucene.util.SampleSameWordContext2;
import org.luncene.util.*;

/**
 * Author: 王俊超
 * Date: 2015-08-31
 * Time: 09:39
 * Declaration: All Rights Reserved !!!
 */
public class TestAnalyzer {
    @Test
    public void test01() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();

//        String txt = "this is my house,I am come from guangdong guangzhou";
        String txt = "this is my house,I am come from guangdong guangzhou,My email is gmail@gmail.com,My QQ is 123456789";

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
        AnalyzerUtils.displayToken(txt, a4);
    }

    @Test
    public void test02() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();
        // Lucene5.3版本使用不了（因为MMSeg版本的原因）
//        Analyzer a5 = new MMSegAnalyzer("data dir path");
        Analyzer a6 = new SmartChineseAnalyzer();

        String txt = "我来自中国广东广州知名地区";

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
        AnalyzerUtils.displayToken(txt, a4);
//        AnalyzerUtils.displayToken(txt, a5);
        AnalyzerUtils.displayToken(txt, a6);
    }

    @Test
    public void test03() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();

        String txt = "how are you thank you";

        AnalyzerUtils.displayAllTokenInfo(txt, a1);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a2);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a3);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a4);
    }

    @Test
    public void test04() {
        Analyzer a1 = new MyStopAnalyzer(new String[]{"i", "you", "hate"});
        Analyzer a2 = new StandardAnalyzer();
        Analyzer a3 = new MyStopAnalyzer();

        String txt = "how are you thank you I hate you";

//        AnalyzerUtils.displayAllTokenInfo(txt, a1);

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
    }

    @Test
    public void test05() {
//        Analyzer a1 = new MySameAnalyzer(new SampleSameWordContext());
        Analyzer a1 = new MySameAnalyzer(new SampleSameWordContext2());

        String txt = "我来自中国广东广州知名地区";
//        String txt = "中国的植物活化石银杏最早出现于3.45亿年前的石炭纪";
//        AnalyzerUtils.displayToken(txt, a1);

        Directory dir = new RAMDirectory();
        try (IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(a1))) {
            Document doc = new Document();
            doc.add(new TextField("content", txt, Field.Store.YES));
            writer.addDocument(doc);
            // 必须要提交才能创建到内存中
            writer.commit();
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
            TopDocs tds = searcher.search(new TermQuery(new Term("content", "中国")), 10);
            Document d = searcher.doc(tds.scoreDocs[0].doc);
            System.out.println(d.get("content"));
            AnalyzerUtils.displayToken(txt, a1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.action.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * 索引搜索类
 * Author: 王俊超
 * Date: 2015-09-11
 * Time: 11:07
 * Declaration: All Rights Reserved !!!
 */
public class Searcher {
    public static void search(String indexDir, String q) throws IOException, ParseException {
        // 搜索的目录
        Directory dir = FSDirectory.open(new File(indexDir));
        // 搜索使用的输入流
        IndexReader reader = IndexReader.open(dir);
        // 索引搜对象
        IndexSearcher is = new IndexSearcher(reader);

        // 查询解析器
        QueryParser parser = new QueryParser(Version.LUCENE_35, "contents",
                new StandardAnalyzer(Version.LUCENE_35));

        // 解析查询，获取查询对象
        Query query = parser.parse(q);

        long start = System.currentTimeMillis();
        // 进行查询，最多获取前10条件数据
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();

        System.out.println("Found " + hits.totalHits + " document(s) (in " + (end - start) +
                " milliseconds) that matched query '" + q + "':");

        // 输出查询出来的对象
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            // 创建文档对象，获取文档对象中的信息
            Document doc = is.doc(scoreDoc.doc);
            System.out.println(doc.get("fullpath"));
        }
    }
}

package com.action.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Author: 王俊超
 * Date: 2015-09-22
 * Time: 10:12
 * Declaration: All Rights Reserved !!!
 */
public class Fragments {
    public void simpleSearch(String idxDir) throws IOException {
        // 搜索的目录
        Directory dir = FSDirectory.open(new File(idxDir));
        // 搜索使用的输入流
        IndexReader reader = IndexReader.open(dir);
        //  // 索引搜对象
        IndexSearcher searcher = new IndexSearcher(reader);

        // 创建一个查询对象
        Query q = new TermQuery(new Term("contents", "lucene"));

        long start = System.currentTimeMillis();
        // 进行查询，最多获取前10条件数据
        TopDocs hits = searcher.search(q, 10);
        long end = System.currentTimeMillis();

        System.out.println("Found " + hits.totalHits + " document(s) (in " + (end - start) +
                " milliseconds) that matched query 'lucene':");

        // 输出查询出来的对象
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            // 创建文档对象，获取文档对象中的信息
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("contents"));
        }

        searcher.close();
    }
}

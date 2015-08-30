package org.itat.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * Author: 王俊超
 * Date: 2015-08-30
 * Time: 10:14
 * Declaration: All Rights Reserved !!!
 */
public class SearcherUtil {
    private Directory directory;
    private IndexReader reader;

    public SearcherUtil() {
        directory = new RAMDirectory();
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

}

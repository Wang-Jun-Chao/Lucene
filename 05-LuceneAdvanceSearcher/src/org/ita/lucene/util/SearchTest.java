package org.ita.lucene.util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.ita.lucene.util.FileIndexUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: 王俊超
 * Date: 2015-09-01
 * Time: 10:35
 * Declaration: All Rights Reserved !!!
 */
public class SearchTest {
    private static IndexReader reader = null;

    static {
        try {
            reader = DirectoryReader.open(FileIndexUtils.getDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IndexSearcher getSearcher() {
        try {
            if (reader == null) {
                reader = DirectoryReader.open(FileIndexUtils.getDirectory());
            } else {
                IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader) reader);

                if (newReader != reader) {
                    reader.close();
                    reader = newReader;
                }

                if (reader == null) {
                    reader = DirectoryReader.open(FileIndexUtils.getDirectory());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new IndexSearcher(reader);
    }

    public void searcher(String queryStr, Sort sort) {
        try {
            IndexSearcher searcher = getSearcher();
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(queryStr);
            TopDocs tds;
            if (sort != null) {
                tds = searcher.search(query, 50, sort);
            } else {
                tds = searcher.search(query, 50);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println(sd.doc + ":(" + sd.score + ")[" + doc.get("filename") + "][" + doc.get("path") + "]["
                        + doc.get("size") + "][" + sdf.format(new Date(Long.parseLong(doc.get("date")))) + "]");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

}

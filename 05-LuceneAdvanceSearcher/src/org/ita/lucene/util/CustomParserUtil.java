package org.ita.lucene.util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 15:39
 * Declaration: All Rights Reserved !!!
 */
public class CustomParserUtil {
    public void searcherByCustomQuery(String value) {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtils.getDirectory()));
            CustomParser parser = new CustomParser("content", new StandardAnalyzer());
            Query  query = parser.parse(value);

            TopDocs tds = searcher.search(query, 500);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.printf("%-5d %-10.10f %-30s %-50s %-8s %-8s %-20s\n",
                        sd.doc,
                        sd.score,
                        doc.get("filename"),
                        doc.get("path"),
                        doc.get("score"),
                        doc.get("size"),
                        sdf.format(new Date(Long.parseLong(doc.get("date")))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
}

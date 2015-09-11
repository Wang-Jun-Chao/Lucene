package org.ita.lucene.util;

import jdk.nashorn.internal.codegen.CompilerConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 18:47
 * Declaration: All Rights Reserved !!!
 */
public class CustomFilter {
    public void searchByCustomFilter() {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtils.getDirectory()));
            Query query = new TermQuery(new Term("content", "java"));
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(query, BooleanClause.Occur.MUST);
            // TopDocs search(Query query, Filter filter, int n)这个方法已经被弃用了，使用BooleanQuery代替
            builder.add(new MyIdFilter(new FilterAccessor() {
                @Override
                public String[] values() {
                    return new String[]{"10", "20", "30", "40", "50", "60"};
                }

                @Override
                public String getField() {
                    return "id";
                }

                @Override
                public boolean set() {
                    return true;
                }
            }), BooleanClause.Occur.MUST);
            TopDocs tds = searcher.search(builder.build(), 500);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.printf("%-5s %-12s %-30s %-50s %-8s %-8s\n",
                    "doc",
                    "score",
                    "filename",
                    "path",
                    "size",
                    "id");

            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.printf("%-5d %-10.10f %-30s %-50s %-8s %-8s\n",
                        sd.doc,
                        sd.score,
                        doc.get("filename"),
                        doc.get("path"),
                        doc.get("size"),
                        doc.get("id"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

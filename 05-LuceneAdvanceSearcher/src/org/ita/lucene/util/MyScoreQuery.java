package org.ita.lucene.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 09:11
 * Declaration: All Rights Reserved !!!
 */
public class MyScoreQuery {

    public void searchByScoreQuery() {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtils.getDirectory()));
            Query query = new TermQuery(new Term("content", "java"));
            // 1、 创建一个评分域
            FunctionQuery fq = new FunctionQuery(new LongFieldSource("score"));
            // 2、根据评分域和原有的Query创建自定义的Query对象
            MyCustomScoreQuery mcsQuery = new MyCustomScoreQuery(query, fq);
            TopDocs tds = searcher.search(mcsQuery, 500);
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
        }
    }

    public void searchByFileScoreQuery() {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtils.getDirectory()));
            Query query = new TermQuery(new Term("content", "java"));
            // 2、根据评分域和原有的Query创建自定义的Query对象
            FilenameScoreQuery mcsQuery = new FilenameScoreQuery(query);
            TopDocs tds = searcher.search(mcsQuery, 500);
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
        }
    }

    public void searchByDateScoreQuery() {
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtils.getDirectory()));
            Query query = new TermQuery(new Term("content", "java"));
            // 2、根据评分域和原有的Query创建自定义的Query对象
            DateScoreQuery mcsQuery = new DateScoreQuery(query);
            TopDocs tds = searcher.search(mcsQuery, 500);
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
        }
    }



    private class FilenameScoreQuery extends CustomScoreQuery {
        public FilenameScoreQuery(Query subQuery) {
            super(subQuery);
        }

        @Override
        protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
            return new FilenameScoreProvider(context);
        }
    }


    private class DateScoreQuery extends CustomScoreQuery {

        public DateScoreQuery(Query subQuery) {
            super(subQuery);
        }

        @Override
        protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
            return new DateScoreProvider(context);
        }
    }

    private class DateScoreProvider extends CustomScoreProvider {
        private IndexReader reader;

        public DateScoreProvider(LeafReaderContext context) {
            super(context);
            reader = context.reader();
        }

        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
            Document docAtHand = reader.document(doc);
            long date = docAtHand.getField("date").numericValue().longValue();
            // 一年内的文件
            if (System.currentTimeMillis() - date < 365 * 24 * 60 * 60 * 100) {
                return subQueryScore * 2;
            }

            return subQueryScore / 2;
        }
    }

    private class FilenameScoreProvider extends CustomScoreProvider {
        private IndexReader reader;

        public FilenameScoreProvider(LeafReaderContext context) {
            super(context);
            reader = context.reader();
        }

        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
            // 如何根据doc获取相应的field的值
            //
            // 在reader没有关闭之前，所有的数据会存储要一个域缓存中，可以通过域缓存获取很多有用的信息
            // filenames = FieldCache.DEFAULT.getStrings(reader, "filename");可以获取所有的filename域的信息

            Document docAtHand = reader.document(doc);
            String itemOrigin = docAtHand.get("filename");

            if (itemOrigin != null && (itemOrigin.endsWith("txt") || itemOrigin.endsWith("ini"))) {
                return subQueryScore * 1.5F;
            }

            return subQueryScore / 1.5F;
        }
    }

    public static class MyCustomScoreQuery extends CustomScoreQuery {

        public MyCustomScoreQuery(Query subQuery, FunctionQuery scoringQuery) {
            super(subQuery, scoringQuery);
        }

        public MyCustomScoreQuery(Query subQuery, FunctionQuery... scoringQueries) {
            super(subQuery, scoringQueries);
        }

        public MyCustomScoreQuery(Query subQuery) {
            super(subQuery);
        }

        @Override
        protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
            // 默认情况实现的评分是通过原有的评分*传入进来的评分域所获取的评分来确定最终打分的
            // 为了根据不同的需求进行评分，需要自己进行评分的设定
            /**
             * 自定评分的步骤
             * 创建一个类继承于CustomScoreProvider
             * 覆盖customScore方法
             */
//            return super.getCustomScoreProvider(context);
            return new MyCustomScoreProvider(context);

        }
    }

    public static class MyCustomScoreProvider extends CustomScoreProvider {

        public MyCustomScoreProvider(LeafReaderContext context) {
            super(context);
        }

        /**
         * @param doc
         * @param subQueryScore 表示默认文档的打分
         * @param valSrcScore   表示的评分域的打分
         * @return
         * @throws IOException
         */
        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
            return subQueryScore / valSrcScore;
        }
    }
}

package org.ita.lucene.util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Author: 王俊超
 * Date: 2015-08-30
 * Time: 19:03
 * Declaration: All Rights Reserved !!!
 */
public class FileIndexUtils {
    private static Directory directory = null;

    static {
        try {
            directory = FSDirectory.open(Paths.get("d:/com.action.lucene/files/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Directory getDirectory() {
        return directory;
    }



    public static void index(boolean hasNew) {
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {

            if (hasNew) {
                writer.deleteAll();
            }

            File dir = new File("d:/com.action.lucene/example/");
            Random random = new Random();
            long count = 0;
            for (File file : dir.listFiles()) {
                Document doc = new Document();
                int score = random.nextInt(600)  ;
                doc.add(new LongField("id", count++, Field.Store.YES));
                doc.add(new TextField("content", new FileReader(file)));
                doc.add(new StringField("filename", file.getName(), Field.Store.YES));
                doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
                doc.add(new LongField("date", file.lastModified(), Field.Store.YES));
                doc.add(new LongField("size", file.length(), Field.Store.YES));
                doc.add(new IntField("score", score, Field.Store.YES));
                // 用作排序使用，在5.2.1后要专门使用这个字段进行排序，使用NumericDocValuesField类型
                doc.add(new NumericDocValuesField("date", file.lastModified()));
                doc.add(new NumericDocValuesField("size", file.length()));
                doc.add(new NumericDocValuesField("score", score));
                writer.addDocument(doc);
            }

            writer.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

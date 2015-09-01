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
            directory = FSDirectory.open(Paths.get("d:/lucene/files/"));
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

            File dir = new File("d:/lucene/example/");
            for (File file : dir.listFiles()) {
                Document doc = new Document();
                doc.add(new TextField("content", new FileReader(file)));
                doc.add(new StringField("filename", file.getName(), Field.Store.YES));
                doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
                doc.add(new LongField("date", file.lastModified(), Field.Store.YES));
                doc.add(new LongField("size", file.length() / 1024, Field.Store.YES));
                writer.addDocument(doc);
            }

            writer.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

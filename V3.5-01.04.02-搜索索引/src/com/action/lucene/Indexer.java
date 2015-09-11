package com.action.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 * 索引器
 * Author: 王俊超
 * Date: 2015-09-11
 * Time: 09:19
 * Declaration: All Rights Reserved !!!
 */
public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDir) throws IOException {
        // 1、创建一个索引存放的目录，indexDir就是索引存放的目录
        Directory dir = FSDirectory.open(new File(indexDir));
        // 2、创建一个写索引的对象
        writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_35,
                new StandardAnalyzer(Version.LUCENE_35)));
        // 删除原来已经存在的索引
        writer.deleteAll();
    }

    // 6、提交索引写入对象的内容
    // 7、关闭索引写入流
    public void close() throws IOException {
        writer.commit();
        writer.close();
    }

    // 对文件进行索引前的过滤操作
    public int index(String dataDir, FileFilter filter) throws Exception {
        File[] files = new File(dataDir).listFiles();

        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()
                    && (filter == null || filter.accept(f))) {
                // 索引文件
                indexFile(f);
            }
        }

        // 返回所写的文档数目
        return writer.numDocs();
    }

    // 3、索引文件
    private void indexFile(File file) throws Exception {
        System.out.println("Indexing " + file.getCanonicalPath());
        // 创建文档对象
        Document doc = getDocument(file);
        // 添加文档
        writer.addDocument(doc);
    }

    // 4、创建文档对象
    protected Document getDocument(File f) throws IOException {
        Document doc = new Document();
        // 5、为文档创建不同的域
        doc.add(new Field("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        // 获取一个标准的文件绝对路径，不会有/aa/bb/cc/dd/../ee/../../ff -> /aa/bb/ff
        doc.add(new Field("fullpath", f.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return doc;
    }
}

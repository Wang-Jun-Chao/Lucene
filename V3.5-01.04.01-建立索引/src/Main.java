import com.action.lucene.Indexer;
import com.action.lucene.TextFilesFilter;
import org.apache.lucene.index.IndexReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Author: 王俊超
 * Date: 2015-09-11
 * Time: 10:34
 * Declaration: All Rights Reserved !!!
 */
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        // 获取文件目录
        String dataDir = String.valueOf(Indexer.class.getResource("/data").getFile());
        dataDir = URLDecoder.decode(dataDir, "UTF-8");
        System.out.println(dataDir);
        // 获取索引文件保存的目录
        String indexDir = String.valueOf(Indexer.class.getResource("/index").getFile());
        indexDir = URLDecoder.decode(indexDir, "UTF-8");
        System.out.println(indexDir);

        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed = 0;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();

        System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
    }
}

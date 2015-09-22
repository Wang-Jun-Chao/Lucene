import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;

import java.io.IOException;

/**
 * Author: 王俊超
 * Date: 2015-09-22
 * Time: 10:32
 * Declaration: All Rights Reserved !!!
 */
public class IndexingTest {
    protected String[] ids = {"1", "2"};
    protected String[] unindexed = {"Netherlands", "Italy"};
    protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
    protected String[] text = {"Amsterdam", "Venice"};

    protected Directory directory;

    @Before
    public void setUp() throws IOException {
        directory = new RAMDirectory();
        IndexWriter writer = getWriter();
    }

    private IndexWriter getWriter() throws IOException {
        return new IndexWriter(directory,
                new IndexWriterConfig(Version.LUCENE_35,
                        new WhitespaceAnalyzer(Version.LUCENE_35)));
    }

    protected int getHitCount(String filedName, String searchString) throws IOException {
        IndexSearcher searcher = new IndexSearcher(IndexReader.open(directory));
        Term t = new Term(filedName, searchString);
        Query query = new TermQuery(t);
        int hitCount =
    }


}

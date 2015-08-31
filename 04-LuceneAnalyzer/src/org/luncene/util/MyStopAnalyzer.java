package org.luncene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.util.CharArraySet;

import java.io.IOException;
import java.util.Set;

/**
 * Author: 王俊超
 * Date: 2015-08-31
 * Time: 10:09
 * Declaration: All Rights Reserved !!!
 */
public class MyStopAnalyzer extends Analyzer {
    private CharArraySet stops;

    public MyStopAnalyzer(String[] sws) {
        // 会自动将字符串数组转换为Set
        stops = StopFilter.makeStopSet(sws, true);
        // 将原有的停用词加入到现在的停用词
        stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
    }

    public MyStopAnalyzer() {
        // 获取原有的停用词
        stops = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    }


    /**
     * 5.3使用的方法
     * @param fieldName
     * @return
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new LetterTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);
        tokenStream = new StopFilter(tokenStream, stops);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}

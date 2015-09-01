package org.itat.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.HMMChineseTokenizer;

/**
 * Author: 王俊超
 * Date: 2015-09-01
 * Time: 08:53
 * Declaration: All Rights Reserved !!!
 */
public class MySameAnalyzer extends Analyzer{

    private SameWordContext swc;

    public MySameAnalyzer(SameWordContext swc) {
        this.swc = swc;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        HMMChineseTokenizer tokenizer = new HMMChineseTokenizer();
        MySameTokenFilter filter = new MySameTokenFilter(tokenizer, swc);
        return new TokenStreamComponents(tokenizer, filter);
    }
}

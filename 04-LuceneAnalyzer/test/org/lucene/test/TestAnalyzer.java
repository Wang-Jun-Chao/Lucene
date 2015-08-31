package org.lucene.test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;
import org.luncene.util.AnalyzerUtils;
import org.luncene.util.MyStopAnalyzer;

/**
 * Author: 王俊超
 * Date: 2015-08-31
 * Time: 09:39
 * Declaration: All Rights Reserved !!!
 */
public class TestAnalyzer {
    @Test
    public void test01() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();

//        String txt = "this is my house,I am come from guangdong guangzhou";
        String txt = "this is my house,I am come from guangdong guangzhou,My email is gmail@gmail.com,My QQ is 123456789";

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
        AnalyzerUtils.displayToken(txt, a4);
    }

    @Test
    public void test02() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();
        // Lucene5.3版本使用不了（因为MMSeg版本的原因）
//        Analyzer a5 = new MMSegAnalyzer("data dir path");
        Analyzer a6 = new SmartChineseAnalyzer();

        String txt = "我来自中国广东广州知名地区";

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
        AnalyzerUtils.displayToken(txt, a4);
//        AnalyzerUtils.displayToken(txt, a5);
        AnalyzerUtils.displayToken(txt, a6);
    }

    @Test
    public void test03() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();

        String txt = "how are you thank you";

        AnalyzerUtils.displayAllTokenInfo(txt, a1);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a2);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a3);
        System.out.println();
        AnalyzerUtils.displayAllTokenInfo(txt, a4);
    }

    @Test
    public void test04() {
        Analyzer a1 = new MyStopAnalyzer(new String[]{"i", "you", "hate"});
        Analyzer a2 = new StandardAnalyzer();
        Analyzer a3 = new MyStopAnalyzer();

        String txt = "how are you thank you I hate you";

//        AnalyzerUtils.displayAllTokenInfo(txt, a1);

        AnalyzerUtils.displayToken(txt, a1);
        AnalyzerUtils.displayToken(txt, a2);
        AnalyzerUtils.displayToken(txt, a3);
    }
}

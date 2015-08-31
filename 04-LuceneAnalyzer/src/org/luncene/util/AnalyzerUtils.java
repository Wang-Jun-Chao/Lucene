package org.luncene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Author: 王俊超
 * Date: 2015-08-31
 * Time: 09:31
 * Declaration: All Rights Reserved !!!
 */
public class AnalyzerUtils {
    public static void displayToken(String str, Analyzer a) {
        try (TokenStream stream = a.tokenStream("content", new StringReader(str))) {
            // 创建一个属性，这个属性会添加流中，随着这个TokenStream增加
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            // 5.3要使用的步骤
            stream.reset();
            while (stream.incrementToken()) {
                System.out.print("[" + cta + "]");
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayAllTokenInfo(String str, Analyzer a) {
        try (TokenStream stream = a.tokenStream("content", new StringReader(str))) {
            // 位置增量的属性，存储语汇单元之间的距离
            PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
            // 每个语汇单元的位置偏移量
            OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
            // 存储每一个语汇单元的信息（分词单元信息）
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            // 使用的分词器的类型信息
            TypeAttribute ta = stream.addAttribute(TypeAttribute.class);
            stream.reset();

            while (stream.incrementToken()) {
                System.out.print(pia.getPositionIncrement() + ":");
                System.out.print(cta + "[" + oa.startOffset() + "-" + oa.endOffset() + "]");
                System.out.print("-->" + ta.type());
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

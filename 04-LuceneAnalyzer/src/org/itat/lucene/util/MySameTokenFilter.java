package org.itat.lucene.util;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.*;

/**
 * Author: 王俊超
 * Date: 2015-09-01
 * Time: 08:54
 * Declaration: All Rights Reserved !!!
 */
public class MySameTokenFilter extends TokenFilter {

    private CharTermAttribute cta = null;
    private PositionIncrementAttribute pia = null;
    private AttributeSource.State current;
    private Deque<String> sames = null;
    private SameWordContext sameWordContext;

    protected MySameTokenFilter(TokenStream input, SameWordContext sameWordContext) {
        super(input);
        cta = input.addAttribute(CharTermAttribute.class);
        pia = input.addAttribute(PositionIncrementAttribute.class);
        sames = new LinkedList<>();
        this.sameWordContext = sameWordContext;
    }

    @Override
    public final boolean incrementToken() throws IOException {


        // 有单词进行处理，处理同义词

//        String[] sames = getSameWord(cta.toString());
//        if (sames != null) {
//            System.out.println(Arrays.toString(sames));
//        }

//        String[] sws = getSameWord(cta.toString());
//        if (sws != null) {
//            // 捕获当前状态
//            current = captureState();
//
//            // 处理同义词
//            for (String s : sws) {
//                cta.setEmpty();
//                cta.append(s);
//            }
//        }

        // 此处处理上一个元素的同义词
        while (sames.size() > 0) {
            // 将元素出栈，并且获取这个同义词
            String str = sames.pop();
            // 还原状态
            restoreState(current);
//            System.out.println("===" + cta);
            cta.setEmpty();
            cta.append(str);
            // 设置位置为0
            pia.setPositionIncrement(0);
            return true;
        }

        // 没有元素
        if (!input.incrementToken()) {
            return false;
        }

        // 准备当前元素的的同义词
        if (addSames(cta.toString())) {
            // 如果有同义词，将当前状态保存
            current = captureState();
        }

        return true;
    }

    private boolean addSames(String key) {

        String[] sws = sameWordContext.getSameWords(key);
        if (sws != null) {
            for (String s : sws) {
                sames.push(s);
            }

            return true;
        }

        return false;
    }
}

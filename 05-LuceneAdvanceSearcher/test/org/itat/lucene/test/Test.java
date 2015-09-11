package org.itat.lucene.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 16:41
 * Declaration: All Rights Reserved !!!
 */
public class Test {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher("2015-09-03") ;
        System.out.println(matcher.matches());
    }
}

package org.itat.lucene.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 王俊超
 * Date: 2015-09-01
 * Time: 10:12
 * Declaration: All Rights Reserved !!!
 */
public class SampleSameWordContext2 implements SameWordContext {
    private Map<String, String[]> map = new HashMap<>();

    public SampleSameWordContext2() {
        map.put("中国", new String[]{"大陆", "天朝"});
//        map.put("我", new String[]{"咱", "俺"});
    }

    @Override
    public String[] getSameWords(String key) {
        return map.get(key);
    }
}

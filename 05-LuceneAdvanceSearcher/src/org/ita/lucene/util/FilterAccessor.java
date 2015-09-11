package org.ita.lucene.util;

/**
 * Author: 王俊超
 * Date: 2015-09-05
 * Time: 08:13
 * Declaration: All Rights Reserved !!!
 */
public interface FilterAccessor {
    public String[] values();

    public String getField();

    public boolean set();
}

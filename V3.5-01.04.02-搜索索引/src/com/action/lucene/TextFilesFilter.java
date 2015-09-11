package com.action.lucene;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤器，只接收文本文件
 * Author: 王俊超
 * Date: 2015-09-11
 * Time: 10:57
 * Declaration: All Rights Reserved !!!
 */
public class TextFilesFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}

package org.ita.lucene.util;

import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 18:36
 * Declaration: All Rights Reserved !!!
 */
public class MyIdFilter extends Filter {
    private FilterAccessor accessor;

    public MyIdFilter(FilterAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public DocIdSet getDocIdSet(LeafReaderContext context, Bits acceptDocs) throws IOException {
        FixedBitSet bits;
        if (accessor.set()) {
            bits = set(context, acceptDocs);
        } else {
            bits = clear(context, acceptDocs);
        }
        return new BitDocIdSet(bits);
    }

    // just for backup
    public DocIdSet getDocIdSet2(LeafReaderContext context, Bits acceptDocs) throws IOException {
        String[] delIds = {"10", "2", "3", "4", "5", "6", "22", "33"};
        FixedBitSet bits = new FixedBitSet(context.reader().maxDoc());//获取没有所有的docid包括未删除的
        bits.set(0, context.reader().maxDoc() - 1);

//        int base = context.docBase;//段的相对基数，保证多个段时相对位置正确
        //int limit=base+arg0.reader().maxDoc();//计算最大限制值
        for (String delId : delIds) {

            // NOTE 这个方法目前还有问题，还不知道怎么获取每一个条目
            bits.clear(Integer.parseInt(delId));
//
////            context.reader().terms("id");
//            PostingsEnum postingsEnum = context.reader().postings(new Term("id", delId));// 必须是唯一的不重复
//
//            System.out.println(delId + " " + postingsEnum);
//
//            //保证是单个不重复的term,如果重复的话，默认会取第一个作为返回结果集,分词后的term也不适用自定义term
//            if (postingsEnum != null) {
//                bits.clear(postingsEnum.docID());//对付符合条件约束的docid循环，不添加到bits里面
//            }
        }
        return new BitDocIdSet(bits);
    }



    @Override
    public String toString(String field) {
        return null;
    }

    private FixedBitSet set(LeafReaderContext context, Bits acceptDocs) {
        FixedBitSet bits = new FixedBitSet(context.reader().maxDoc());
        try {
            for (String delId : accessor.values()) {
                // TODO 不知道为什么这里总是返回null
                PostingsEnum pe = context.reader().postings(new Term(accessor.getField(), delId));

                if (pe != null) {
                    bits.set(pe.docID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bits;
    }

    private FixedBitSet clear(LeafReaderContext context, Bits acceptDocs) {
        FixedBitSet bits = new FixedBitSet(context.reader().maxDoc());
        bits.set(0, context.reader().maxDoc() - 1);
        try {

            for (String delId : accessor.values()) {
                // TODO 不知道为什么这里总是返回null
                PostingsEnum pe = context.reader().postings(new Term(accessor.getField(), delId));

                if (pe != null) {
                    bits.clear(pe.docID());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bits;
    }
}

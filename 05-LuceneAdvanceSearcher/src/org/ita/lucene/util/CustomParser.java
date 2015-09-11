package org.ita.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.CharStream;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserTokenManager;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * Author: 王俊超
 * Date: 2015-09-03
 * Time: 15:34
 * Declaration: All Rights Reserved !!!
 */
public class CustomParser extends QueryParser {
    public CustomParser(String f, Analyzer a) {
        super(f, a);
    }

    protected CustomParser(CharStream stream) {
        super(stream);
    }

    protected CustomParser(QueryParserTokenManager tm) {
        super(tm);
    }

    @Override
    protected Query getWildcardQuery(String field, String termStr) throws ParseException {
        throw new ParseException("由于性能原因，已经禁用通配符查询，请使用更精确的查询");
    }

    @Override
    protected Query getFuzzyQuery(String field, String termStr, float minSimilarity) throws ParseException {
        throw new ParseException("由于性能原因，已经禁用模糊查询，请使用更精确的查询");
    }

    @Override
    protected Query getRangeQuery(String field, String part1, String part2,
                                  boolean startInclusive, boolean endInclusive) throws ParseException {

        if ("size".equals(field)) {
            return NumericRangeQuery.newLongRange(field, Long.parseLong(part1),
                    Long.parseLong(part2), startInclusive, endInclusive);
        } else if ("date".equals(field)) {

            String dataType = "yyyy-MM-dd";
            if (part1 != null && part1.matches("\\d{4}-\\d{2}-\\d{2}")
                    && part2 != null && part2.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(dataType);

                    long start = sdf.parse(part1).getTime();
                    long end = sdf.parse(part2).getTime();

                    return NumericRangeQuery.newLongRange(field, start, end, startInclusive, endInclusive);

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ParseException("要检索的日期时间格式不正确。请使用：" + dataType + "上期类型");
            }

            String dateType = "yyyy-MM-dd";
            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

        }

        return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
    }
}

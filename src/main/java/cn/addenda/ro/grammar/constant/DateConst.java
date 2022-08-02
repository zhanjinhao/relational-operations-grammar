package cn.addenda.ro.grammar.constant;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author addenda
 * @datetime 2021/8/6 12:12
 */
public class DateConst {

    public static final String DAY_STR = "day";
    public static final String WEEK_STR = "week";
    public static final String MONTH_STR = "month";
    public static final String YEAR_STR = "year";
    public static final String QUARTER_STR = "quarter";
    public static final String HOUR_STR = "hour";
    public static final String MINUTE_STR = "minute";
    public static final String SECOND_STR = "second";
    public static final String MICROSECOND_STR = "microsecond";

    public static final String YEAR_FORMAT_STR = "%Y";
    public static final String MONTH_FORMAT_STR = "%m";
    public static final String DAY_FORMAT_STR = "%d";
    public static final String HOUR_FORMAT_STR = "%H";
    public static final String MINUTE_FORMAT_STR = "%i";
    public static final String SECOND_FORMAT_STR = "%s";
    public static final String MICROSECOND_FORMAT_STR = "%f";

    public static final Token YEAR_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.YEAR_FORMAT_STR);
    public static final Token MONTH_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.MONTH_FORMAT_STR);
    public static final Token DAY_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.DAY_FORMAT_STR);
    public static final Token HOUR_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.HOUR_FORMAT_STR);
    public static final Token MINUTE_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.MINUTE_FORMAT_STR);
    public static final Token SECOND_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.SECOND_FORMAT_STR);
    public static final Token MICROSECOND_FORMAT = new Token(TokenType.IDENTIFIER, DateConst.MICROSECOND_FORMAT_STR);


    public static final Token MICROSECOND = new Token(TokenType.IDENTIFIER, DateConst.MICROSECOND_STR);
    public static final Token SECOND = new Token(TokenType.IDENTIFIER, DateConst.SECOND_STR);
    public static final Token MINUTE = new Token(TokenType.IDENTIFIER, DateConst.MINUTE_STR);
    public static final Token HOUR = new Token(TokenType.IDENTIFIER, DateConst.HOUR_STR);
    public static final Token DAY = new Token(TokenType.IDENTIFIER, DateConst.DAY_STR);
    public static final Token WEEK = new Token(TokenType.IDENTIFIER, DateConst.WEEK_STR);
    public static final Token MONTH = new Token(TokenType.IDENTIFIER, DateConst.MONTH_STR);
    public static final Token QUARTER = new Token(TokenType.IDENTIFIER, DateConst.QUARTER_STR);
    public static final Token YEAR = new Token(TokenType.IDENTIFIER, DateConst.YEAR_STR);

    private static final Map<String, Token> patternToToken = new HashMap<>();

    static {
        patternToToken.put(YEAR_FORMAT_STR, YEAR_FORMAT);
        patternToToken.put(MONTH_FORMAT_STR, MONTH_FORMAT);
        patternToToken.put(DAY_FORMAT_STR, DAY_FORMAT);
        patternToToken.put(HOUR_FORMAT_STR, HOUR_FORMAT);
        patternToToken.put(MINUTE_FORMAT_STR, MINUTE_FORMAT);
        patternToToken.put(SECOND_FORMAT_STR, SECOND_FORMAT);
        patternToToken.put(MICROSECOND_FORMAT_STR, MICROSECOND_FORMAT);
    }

    private static final Map<String, TemporalField> patternToJava8TemporalField = new HashMap<>();

    static {
        patternToJava8TemporalField.put(YEAR_FORMAT_STR, ChronoField.YEAR);
        patternToJava8TemporalField.put(MONTH_FORMAT_STR, ChronoField.MONTH_OF_YEAR);
        patternToJava8TemporalField.put(DAY_FORMAT_STR, ChronoField.DAY_OF_MONTH);
        patternToJava8TemporalField.put(HOUR_FORMAT_STR, ChronoField.HOUR_OF_DAY);
        patternToJava8TemporalField.put(MINUTE_FORMAT_STR, ChronoField.MINUTE_OF_HOUR);
        patternToJava8TemporalField.put(SECOND_FORMAT_STR, ChronoField.SECOND_OF_MINUTE);
        patternToJava8TemporalField.put(MICROSECOND_FORMAT_STR, ChronoField.MICRO_OF_SECOND);
    }

    private static final Map<String, Integer> patternToJava1TemporalField = new HashMap<>();

    static {
        patternToJava1TemporalField.put(YEAR_FORMAT_STR, Calendar.YEAR);
        patternToJava1TemporalField.put(MONTH_FORMAT_STR, Calendar.MONTH);
        patternToJava1TemporalField.put(DAY_FORMAT_STR, Calendar.DAY_OF_MONTH);
        patternToJava1TemporalField.put(HOUR_FORMAT_STR, Calendar.HOUR_OF_DAY);
        patternToJava1TemporalField.put(MINUTE_FORMAT_STR, Calendar.MINUTE);
        patternToJava1TemporalField.put(SECOND_FORMAT_STR, Calendar.SECOND);
        patternToJava1TemporalField.put(MICROSECOND_FORMAT_STR, Calendar.MILLISECOND);
    }

    public static Token getToken(String pattern) {
        return patternToToken.get(pattern);
    }

}

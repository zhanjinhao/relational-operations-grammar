package cn.addenda.ro.grammar.lexical.token;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/3/1 22:24
 */
public class TokenTypeLexemeMapping {

    private TokenTypeLexemeMapping() {
    }

    private static final Map<String, TokenType> lexemeToTokenType = new HashMap<>();
    private static final Map<TokenType, String> tokenTypeToLexeme = new HashMap<>();

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String STAR = "*";
    public static final String SLASH = "/";

    public static final String LEFT_PAREN = "(";
    public static final String RIGHT_PAREN = ")";

    public static final String COMMA = ",";

    public static final String BANG = "!";
    public static final String BANG_EQUAL = "!=";
    public static final String EQUAL = "=";
    public static final String GREATER = ">";
    public static final String GREATER_EQUAL = ">=";
    public static final String LESS = "<";
    public static final String LESS_EQUAL = "<=";
    public static final String PARAMETER = "?";

    public static final String TRUE = "true";
    public static final String NULL = "null";
    public static final String FALSE = "false";

    public static final String AND = "and";
    public static final String OR = "or";

    public static final String ORDER = "order";
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public static final String GROUP = "group";
    public static final String FLAT = "flat";
    public static final String SUM = "sum";
    public static final String COUNT = "count";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String AVG = "avg";
    public static final String GROUP_CONCAT = "group_concat";
    public static final String SEPARATOR = "separator";

    public static final String WHERE = "where";
    public static final String HAVING = "having";
    public static final String EXISTS = "exists";
    public static final String IN = "in";
    public static final String IS = "is";
    public static final String LIKE = "like";
    public static final String CONTAINS = "contains";

    public static final String RIGHT = "right";
    public static final String CROSS = "cross";
    public static final String LEFT = "left";
    public static final String JOIN = "join";

    public static final String CUNION = "union";
    public static final String CMINUS = "minus";
    public static final String CINTERSECT = "intersect";
    public static final String CEXCEPT = "except";
    public static final String ALL = "all";

    public static final String DISTINCT = "distinct";

    public static final String SELECT = "select";
    public static final String AS = "as";
    public static final String BY = "by";
    public static final String NOT = "not";

    public static final String INSERT = "insert";
    public static final String INTO = "into";
    public static final String IGNORE = "ignore";
    public static final String DUPLICATE = "duplicate";
    public static final String KEY = "key";
    public static final String VALUES = "values";

    public static final String DELETE = "delete";

    public static final String UPDATE = "update";

    public static final String CASE = "case";
    public static final String WHEN = "when";
    public static final String THEN = "then";
    public static final String ELSE = "else";
    public static final String END = "end";

    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    public static final String FROM = "from";
    public static final String SET = "set";
    public static final String ON = "on";

    public static final String INTERVAL = "interval";

    public static final String LOCK = "lock";
    public static final String SHARE = "share";
    public static final String MODE = "mode";
    public static final String FOR = "for";

    public static final String OVER = "over";
    public static final String PARTITION = "partition";
    public static final String ROWS = "rows";
    public static final String RANGE = "range";
    public static final String BETWEEN = "between";
    public static final String UNBOUNDED = "unbounded";
    public static final String CURRENT = "current";
    public static final String PRECEDING = "preceding";
    public static final String FOLLOWING = "following";
    public static final String ROW = "row";

    public static final String CUME_DIST = "cume_dist";
    public static final String DENSE_RANK = "dense_rank";
    public static final String FIRST_VALUE = "first_value";
    public static final String LAG = "lag";
    public static final String LAST_VALUE = "last_value";
    public static final String LEAD = "lead";
    public static final String NTH_VALUE = "nth_value";
    public static final String NTILE = "ntile";
    public static final String PERCENT_RANK = "percent_rank";
    public static final String RANK = "rank";
    public static final String ROW_NUMBER = "row_number";

    static {
        // symbol
        lexemeToTokenType.put(PLUS, TokenType.PLUS);
        lexemeToTokenType.put(MINUS, TokenType.MINUS);
        lexemeToTokenType.put(STAR, TokenType.STAR);
        lexemeToTokenType.put(SLASH, TokenType.SLASH);
        lexemeToTokenType.put(LEFT_PAREN, TokenType.LEFT_PAREN);
        lexemeToTokenType.put(RIGHT_PAREN, TokenType.RIGHT_PAREN);
        lexemeToTokenType.put(COMMA, TokenType.COMMA);
        lexemeToTokenType.put(BANG, TokenType.BANG);
        lexemeToTokenType.put(BANG_EQUAL, TokenType.BANG_EQUAL);
        lexemeToTokenType.put(EQUAL, TokenType.EQUAL);
        lexemeToTokenType.put(GREATER, TokenType.GREATER);
        lexemeToTokenType.put(GREATER_EQUAL, TokenType.GREATER_EQUAL);
        lexemeToTokenType.put(LESS, TokenType.LESS);
        lexemeToTokenType.put(LESS_EQUAL, TokenType.LESS_EQUAL);
        lexemeToTokenType.put(PARAMETER, TokenType.PARAMETER);

        // PRIMARY
        lexemeToTokenType.put(TRUE, TokenType.TRUE);
        lexemeToTokenType.put(NULL, TokenType.NULL);
        lexemeToTokenType.put(FALSE, TokenType.FALSE);

        // LOGIC
        lexemeToTokenType.put(AND, TokenType.AND);
        lexemeToTokenType.put(OR, TokenType.OR);

        // ORDER
        lexemeToTokenType.put(ORDER, TokenType.ORDER);
        lexemeToTokenType.put(ASC, TokenType.ASC);
        lexemeToTokenType.put(DESC, TokenType.DESC);

        // GROUP
        lexemeToTokenType.put(GROUP, TokenType.GROUP);
        lexemeToTokenType.put(FLAT, TokenType.FLAT);
        lexemeToTokenType.put(SUM, TokenType.SUM);
        lexemeToTokenType.put(AVG, TokenType.AVG);
        lexemeToTokenType.put(COUNT, TokenType.COUNT);
        lexemeToTokenType.put(MIN, TokenType.MIN);
        lexemeToTokenType.put(MAX, TokenType.MAX);
        lexemeToTokenType.put(GROUP_CONCAT, TokenType.GROUP_CONCAT);
        lexemeToTokenType.put(SEPARATOR, TokenType.SEPARATOR);

        // CONDITION
        lexemeToTokenType.put(WHERE, TokenType.WHERE);
        lexemeToTokenType.put(HAVING, TokenType.HAVING);
        lexemeToTokenType.put(EXISTS, TokenType.EXISTS);
        lexemeToTokenType.put(IN, TokenType.IN);
        lexemeToTokenType.put(IS, TokenType.IS);
        lexemeToTokenType.put(LIKE, TokenType.LIKE);
        lexemeToTokenType.put(CONTAINS, TokenType.CONTAINS);

        // JOIN
        lexemeToTokenType.put(RIGHT, TokenType.RIGHT);
        lexemeToTokenType.put(CROSS, TokenType.CROSS);
        lexemeToTokenType.put(LEFT, TokenType.LEFT);
        lexemeToTokenType.put(JOIN, TokenType.JOIN);

        // SET OPERATION
        lexemeToTokenType.put(CUNION, TokenType.CUNION);
        lexemeToTokenType.put(CMINUS, TokenType.CMINUS);
        lexemeToTokenType.put(CINTERSECT, TokenType.CINTERSECT);
        lexemeToTokenType.put(CEXCEPT, TokenType.CEXCEPT);
        lexemeToTokenType.put(ALL, TokenType.ALL);

        // RESULT QUALIFICATION
        lexemeToTokenType.put(DISTINCT, TokenType.DISTINCT);

        // SELECT
        lexemeToTokenType.put(SELECT, TokenType.SELECT);
        lexemeToTokenType.put(AS, TokenType.AS);
        lexemeToTokenType.put(BY, TokenType.BY);
        lexemeToTokenType.put(NOT, TokenType.NOT);

        // INSERT
        lexemeToTokenType.put(INSERT, TokenType.INSERT);
        lexemeToTokenType.put(INTO, TokenType.INTO);
        lexemeToTokenType.put(IGNORE, TokenType.IGNORE);
        lexemeToTokenType.put(DUPLICATE, TokenType.DUPLICATE);
        lexemeToTokenType.put(KEY, TokenType.KEY);
        lexemeToTokenType.put(VALUES, TokenType.VALUES);

        // DELETE
        lexemeToTokenType.put(DELETE, TokenType.DELETE);

        // UPDATE
        lexemeToTokenType.put(UPDATE, TokenType.UPDATE);

        // LIMIT
        lexemeToTokenType.put(LIMIT, TokenType.LIMIT);
        lexemeToTokenType.put(OFFSET, TokenType.OFFSET);

        // COMMON
        lexemeToTokenType.put(FROM, TokenType.FROM);
        lexemeToTokenType.put(SET, TokenType.SET);
        lexemeToTokenType.put(ON, TokenType.ON);

        // TIME
        lexemeToTokenType.put(INTERVAL, TokenType.INTERVAL);

        // SELECT 锁
        lexemeToTokenType.put(LOCK, TokenType.LOCK);
        lexemeToTokenType.put(SHARE, TokenType.SHARE);
        lexemeToTokenType.put(MODE, TokenType.MODE);
        lexemeToTokenType.put(FOR, TokenType.FOR);


        // WINDOW
        lexemeToTokenType.put(OVER, TokenType.OVER);
        lexemeToTokenType.put(PARTITION, TokenType.PARTITION);
        lexemeToTokenType.put(ROWS, TokenType.ROWS);
        lexemeToTokenType.put(RANGE, TokenType.RANGE);
        lexemeToTokenType.put(BETWEEN, TokenType.BETWEEN);
        lexemeToTokenType.put(UNBOUNDED, TokenType.UNBOUNDED);
        lexemeToTokenType.put(CURRENT, TokenType.CURRENT);
        lexemeToTokenType.put(PRECEDING, TokenType.PRECEDING);
        lexemeToTokenType.put(FOLLOWING, TokenType.FOLLOWING);
        lexemeToTokenType.put(ROW, TokenType.ROW);


        // WINDOW FUNCTION
        lexemeToTokenType.put(CUME_DIST, TokenType.CUME_DIST);
        lexemeToTokenType.put(DENSE_RANK, TokenType.DENSE_RANK);
        lexemeToTokenType.put(FIRST_VALUE, TokenType.FIRST_VALUE);
        lexemeToTokenType.put(LAG, TokenType.LAG);
        lexemeToTokenType.put(LAST_VALUE, TokenType.LAST_VALUE);
        lexemeToTokenType.put(LEAD, TokenType.LEAD);
        lexemeToTokenType.put(NTH_VALUE, TokenType.NTH_VALUE);
        lexemeToTokenType.put(NTILE, TokenType.NTILE);
        lexemeToTokenType.put(PERCENT_RANK, TokenType.PERCENT_RANK);
        lexemeToTokenType.put(RANK, TokenType.RANK);
        lexemeToTokenType.put(ROW_NUMBER, TokenType.ROW_NUMBER);


        // CASE WHEN
        lexemeToTokenType.put(CASE, TokenType.CASE);
        lexemeToTokenType.put(WHEN, TokenType.WHEN);
        lexemeToTokenType.put(THEN, TokenType.THEN);
        lexemeToTokenType.put(ELSE, TokenType.ELSE);

        lexemeToTokenType.put(END, TokenType.END);

        Set<Map.Entry<String, TokenType>> entries = lexemeToTokenType.entrySet();
        for (Map.Entry<String, TokenType> entry : entries) {
            TokenType tokenType = entry.getValue();
            String lexeme = entry.getKey();
            tokenTypeToLexeme.put(tokenType, lexeme);
        }
    }

    public static String getLexeme(TokenType tokenType) {
        return tokenType == null ? null : tokenTypeToLexeme.get(tokenType);
    }

    public static TokenType getTokenType(String lexeme) {
        return lexeme == null ? null : lexemeToTokenType.get(lexeme);
    }

}

package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.grammar.ast.update.UpdateParser;
import cn.addenda.ro.grammar.lexical.scan.DefaultScanner;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @datetime 2022/9/6 15:59
 */
public class CurdUtils {

    private CurdUtils() {
    }

    private static final Map<TokenSequence, Curd> TOKEN_SEQUENCE_CURD_MAP = new ConcurrentHashMap<>();

    public static Curd parse(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            CurdParser curdParser = CurdParserFactory.createCurdParser(tokenSequence);
            return curdParser.parse();
        }).deepClone();
    }

    public static Curd parseExpression(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            ExpressionParser parser = CurdParserFactory.createExpressionParser(tokenSequence);
            return parser.parse();
        }).deepClone();
    }

    public static Curd parseInsert(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            InsertParser parser = CurdParserFactory.createInsertParser(tokenSequence);
            return parser.parse();
        }).deepClone();
    }

    public static Curd parseUpdate(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            UpdateParser parser = CurdParserFactory.createUpdateParser(tokenSequence);
            return parser.parse();
        }).deepClone();
    }

    public static Curd parseDelete(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            DeleteParser parser = CurdParserFactory.createDeleteParser(tokenSequence);
            return parser.parse();
        }).deepClone();
    }

    public static Curd parseSelect(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP.computeIfAbsent(tokenSequence, s -> {
            SelectParser parser = CurdParserFactory.createSelectParser(tokenSequence);
            return parser.parse();
        }).deepClone();
    }

}

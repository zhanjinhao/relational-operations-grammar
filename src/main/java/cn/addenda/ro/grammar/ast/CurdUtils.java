package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.DefaultFunctionEvaluator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
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

    private static final Map<String, Map<TokenSequence, Curd>> TOKEN_SEQUENCE_CURD_MAP = new ConcurrentHashMap<>();

    public static Curd parse(String sql) {
        return parse(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parse(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createCurdParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

    public static Curd parseExpression(String sql) {
        return parseExpression(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseExpression(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createExpressionParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

    public static Curd parseInsert(String sql) {
        return parseInsert(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseInsert(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createInsertParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

    public static Curd parseUpdate(String sql) {
        return parseUpdate(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseUpdate(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createUpdateParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

    public static Curd parseDelete(String sql) {
        return parseDelete(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseDelete(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createDeleteParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

    public static Curd parseSelect(String sql) {
        return parseSelect(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseSelect(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createSelectParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

}

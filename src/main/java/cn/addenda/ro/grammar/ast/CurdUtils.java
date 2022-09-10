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
        return parse(tokenSequence, functionEvaluator);
    }

    public static Curd parse(TokenSequence tokenSequence) {
        return parse(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parse(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
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
        return parseExpression(tokenSequence, functionEvaluator);
    }

    public static Curd parseExpression(TokenSequence tokenSequence) {
        return parseExpression(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseExpression(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
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
        return parseInsert(tokenSequence, functionEvaluator);
    }

    public static Curd parseInsert(TokenSequence tokenSequence) {
        return parseInsert(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseInsert(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
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
        return parseUpdate(tokenSequence, functionEvaluator);
    }

    public static Curd parseUpdate(TokenSequence tokenSequence) {
        return parseUpdate(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseUpdate(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
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
        return parseDelete(tokenSequence, functionEvaluator);
    }

    public static Curd parseDelete(TokenSequence tokenSequence) {
        return parseDelete(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseDelete(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
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
        return parseSelect(tokenSequence, functionEvaluator);
    }

    public static Curd parseSelect(TokenSequence tokenSequence) {
        return parseSelect(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static Curd parseSelect(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator) {
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createSelectParser(tokenSequence, functionEvaluator).parse())
                .deepClone();
    }

}

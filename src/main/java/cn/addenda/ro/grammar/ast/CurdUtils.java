package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.Insert;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.update.Update;
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
        return parse(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Curd parse(String sql, boolean detectAstMetaData) {
        return parse(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Curd parse(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parse(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Curd parse(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parse(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Curd parse(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createCurdParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone();
    }

    public static Curd parseExpression(String sql) {
        return parseExpression(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Curd parseExpression(String sql, boolean detectAstMetaData) {
        return parseExpression(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Curd parseExpression(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parseExpression(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Curd parseExpression(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parseExpression(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Curd parseExpression(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createExpressionParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone();
    }

    public static Insert parseInsert(String sql) {
        return parseInsert(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Insert parseInsert(String sql, boolean detectAstMetaData) {
        return parseInsert(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Insert parseInsert(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parseInsert(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Insert parseInsert(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parseInsert(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Insert parseInsert(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return (Insert) TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createInsertParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone(detectAstMetaData);
    }

    public static Update parseUpdate(String sql) {
        return parseUpdate(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Update parseUpdate(String sql, boolean detectAstMetaData) {
        return parseUpdate(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Update parseUpdate(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parseUpdate(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Update parseUpdate(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parseUpdate(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Update parseUpdate(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return (Update) TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createUpdateParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone(detectAstMetaData);
    }

    public static Delete parseDelete(String sql) {
        return parseDelete(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Delete parseDelete(String sql, boolean detectAstMetaData) {
        return parseDelete(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Delete parseDelete(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parseDelete(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Delete parseDelete(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parseDelete(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Delete parseDelete(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return (Delete) TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createDeleteParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone(detectAstMetaData);
    }

    public static Select parseSelect(String sql) {
        return parseSelect(sql, DefaultFunctionEvaluator.getInstance(), true);
    }

    public static Select parseSelect(String sql, boolean detectAstMetaData) {
        return parseSelect(sql, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Select parseSelect(String sql, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return parseSelect(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static Select parseSelect(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return parseSelect(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static Select parseSelect(TokenSequence tokenSequence, FunctionEvaluator<? extends FunctionDescriptor> functionEvaluator, boolean detectAstMetaData) {
        return (Select) TOKEN_SEQUENCE_CURD_MAP
                .computeIfAbsent(functionEvaluator.name(), s -> new ConcurrentHashMap<>())
                .computeIfAbsent(tokenSequence, s -> CurdParserFactory.createSelectParser(tokenSequence, functionEvaluator, detectAstMetaData).parse())
                .deepClone(detectAstMetaData);
    }

}

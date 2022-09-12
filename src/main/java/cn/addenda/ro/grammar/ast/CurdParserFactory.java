package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.grammar.ast.update.UpdateParser;
import cn.addenda.ro.grammar.function.evaluator.DefaultFunctionEvaluator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.DefaultScanner;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;

/**
 * @author addenda
 * @datetime 2021/7/24 22:27
 */
public class CurdParserFactory {

    private CurdParserFactory() {
    }

    public static ExpressionParser createExpressionParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new ExpressionParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static ExpressionParser createExpressionParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createExpressionParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static ExpressionParser createExpressionParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createExpressionParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static ExpressionParser createExpressionParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createExpressionParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static SelectParser createSelectParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new SelectParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static SelectParser createSelectParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createSelectParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static SelectParser createSelectParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createSelectParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static SelectParser createSelectParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createSelectParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }


    public static InsertParser createInsertParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new InsertParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static InsertParser createInsertParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createInsertParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static InsertParser createInsertParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createInsertParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static InsertParser createInsertParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createInsertParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }


    public static UpdateParser createUpdateParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new UpdateParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static UpdateParser createUpdateParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createUpdateParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static UpdateParser createUpdateParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createUpdateParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static UpdateParser createUpdateParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createUpdateParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }


    public static DeleteParser createDeleteParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new DeleteParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static DeleteParser createDeleteParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createDeleteParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static DeleteParser createDeleteParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createDeleteParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static DeleteParser createDeleteParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createDeleteParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static CurdParser createCurdParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        return new CurdParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static CurdParser createCurdParser(String sql, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createCurdParser(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    public static CurdParser createCurdParser(TokenSequence tokenSequence, boolean detectAstMetaData) {
        return createCurdParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

    public static CurdParser createCurdParser(String sql, boolean detectAstMetaData) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createCurdParser(tokenSequence, DefaultFunctionEvaluator.getInstance(), detectAstMetaData);
    }

}

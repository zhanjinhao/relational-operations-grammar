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

    public static ExpressionParser createExpressionParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new ExpressionParser(tokenSequence, functionEvaluator);
    }

    public static ExpressionParser createExpressionParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createExpressionParser(tokenSequence, functionEvaluator);
    }

    public static ExpressionParser createExpressionParser(TokenSequence tokenSequence) {
        return createExpressionParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static ExpressionParser createExpressionParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createExpressionParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static SelectParser createSelectParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new SelectParser(tokenSequence, functionEvaluator);
    }

    public static SelectParser createSelectParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createSelectParser(tokenSequence, functionEvaluator);
    }

    public static SelectParser createSelectParser(TokenSequence tokenSequence) {
        return createSelectParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static SelectParser createSelectParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createSelectParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }


    public static InsertParser createInsertParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new InsertParser(tokenSequence, functionEvaluator);
    }

    public static InsertParser createInsertParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createInsertParser(tokenSequence, functionEvaluator);
    }

    public static InsertParser createInsertParser(TokenSequence tokenSequence) {
        return createInsertParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static InsertParser createInsertParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createInsertParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }


    public static UpdateParser createUpdateParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new UpdateParser(tokenSequence, functionEvaluator);
    }

    public static UpdateParser createUpdateParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createUpdateParser(tokenSequence, functionEvaluator);
    }

    public static UpdateParser createUpdateParser(TokenSequence tokenSequence) {
        return createUpdateParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static UpdateParser createUpdateParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createUpdateParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }


    public static DeleteParser createDeleteParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new DeleteParser(tokenSequence, functionEvaluator);
    }

    public static DeleteParser createDeleteParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createDeleteParser(tokenSequence, functionEvaluator);
    }

    public static DeleteParser createDeleteParser(TokenSequence tokenSequence) {
        return createDeleteParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static DeleteParser createDeleteParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createDeleteParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static CurdParser createCurdParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator) {
        return new CurdParser(tokenSequence, functionEvaluator);
    }

    public static CurdParser createCurdParser(String sql, FunctionEvaluator<?> functionEvaluator) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createCurdParser(tokenSequence, functionEvaluator);
    }

    public static CurdParser createCurdParser(TokenSequence tokenSequence) {
        return createCurdParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

    public static CurdParser createCurdParser(String sql) {
        TokenSequence tokenSequence = new DefaultScanner(sql).scanTokens();
        return createCurdParser(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }

}

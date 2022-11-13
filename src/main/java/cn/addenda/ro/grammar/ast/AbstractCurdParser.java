package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.Parser;
import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.UpdateParser;
import cn.addenda.ro.grammar.constant.DateConst;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/7/22 19:16
 */
public abstract class AbstractCurdParser implements Parser<Curd>, ROErrorReporter {

    protected final TokenSequence tokenSequence;

    private final FunctionEvaluator<?> functionEvaluator;

    private final Set<String> functionNameSet = new HashSet<>();

    protected final ROErrorReporter errorReporterDelegate;

    protected boolean detectAstMetaData;

    protected AbstractCurdParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        this.errorReporterDelegate = new AstROErrorReporterDelegate(tokenSequence);
        this.tokenSequence = tokenSequence;
        this.functionEvaluator = functionEvaluator;
        functionNameSet.addAll(functionEvaluator.functionNameSet());
        this.detectAstMetaData = detectAstMetaData;
    }

    @Override
    public void error(int errorCode) {
        errorReporterDelegate.error(errorCode);
    }

    protected void saveSelectType(Curd curd, SingleSelectType singleSelectType, SelectType selectType) {
        if (curd instanceof SingleSelect) {
            SingleSelect singleSelect = (SingleSelect) curd;
            if (singleSelect.getSingleSelectType() == null) {
                singleSelect.setSingleSelectType(singleSelectType);
            }
        } else if (curd instanceof Select) {
            Select select = (Select) curd;
            saveSelectType(select.getLeftCurd(), singleSelectType, selectType);
            saveSelectType(select.getRightCurd(), singleSelectType, selectType);
            if (select.getSelectType() == null) {
                select.setSelectType(selectType);
            }
        } else if (curd instanceof BinaryArithmetic) {
            BinaryArithmetic binaryArithmetic = (BinaryArithmetic) curd;
            saveSelectType(binaryArithmetic.getLeftCurd(), singleSelectType, selectType);
            saveSelectType(binaryArithmetic.getRightCurd(), singleSelectType, selectType);
        } else if (curd instanceof UnaryArithmetic) {
            UnaryArithmetic unaryArithmetic = (UnaryArithmetic) curd;
            saveSelectType(unaryArithmetic.getCurd(), singleSelectType, selectType);
        } else if (curd instanceof Comparison) {
            Comparison comparison = (Comparison) curd;
            saveSelectType(comparison.getLeftCurd(), singleSelectType, selectType);
            saveSelectType(comparison.getRightCurd(), singleSelectType, selectType);
        } else if (curd instanceof Logic) {
            Logic logic = (Logic) curd;
            saveSelectType(logic.getLeftCurd(), singleSelectType, selectType);
            saveSelectType(logic.getRightCurd(), singleSelectType, selectType);
        }
    }


    protected void doFunctionStaticCheck(Function function) {
        Class<? extends AbstractCurdParser> aClass = this.getClass();

        if (aClass.isAssignableFrom(SelectParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.SELECT);
        } else if (aClass.isAssignableFrom(UpdateParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.UPDATE);
        } else if (aClass.isAssignableFrom(InsertParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.INSERT);
        } else if (aClass.isAssignableFrom(DeleteParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.DELETE);
        } else if (aClass.isAssignableFrom(ExpressionParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.EXPRESSION);
        }

    }

    public FunctionEvaluator<?> getFunctionEvaluator() {
        return functionEvaluator;
    }

    protected boolean checkFunction(Token current, Token next) {
        if (current == null || next == null) {
            return false;
        }
        return checkFunctionName(current) && TokenType.LEFT_PAREN.equals(next.getType());
    }

    protected boolean checkTimeUnit(Token current, Token next) {
        if (current == null || next == null) {
            return false;
        }
        return (DateConst.YEAR.equals(current) || DateConst.MONTH.equals(current) || DateConst.DAY.equals(current)
                || DateConst.HOUR.equals(current) || DateConst.MINUTE.equals(current)
                || DateConst.SECOND.equals(current) || DateConst.MICROSECOND.equals(current)) && TokenType.FROM.equals(next.getType());
    }

    private boolean checkFunctionName(Token token) {
        if (token == null) {
            return false;
        }
        return TokenType.IDENTIFIER.equals(token.getType()) && functionNameSet.contains((String) token.getLiteral());
    }

}

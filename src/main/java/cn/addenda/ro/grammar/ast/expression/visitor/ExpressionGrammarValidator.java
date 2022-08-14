package cn.addenda.ro.grammar.ast.expression.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.LogicGrammarValidationDelegate;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.GroupFunction;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelect;
import cn.addenda.ro.grammar.constant.DateConst;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/12/16 19:14
 */
public class ExpressionGrammarValidator extends ExpressionVisitorForDelegation<Void> {

    public ExpressionGrammarValidator(CurdVisitor<Void> client, ROErrorReporter roErrorReporter) {
        super(client);
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitWhereSeg(WhereSeg whereSeg) {
        Curd logic = whereSeg.getLogic();
        if (logic == null || !LogicGrammarValidationDelegate.checkBooleanResult(logic)) {
            error(AstROErrorReporterDelegate.EXPRESSION_logic_VALIDATION);
            return null;
        }
        return logic.accept(this);
    }

    @Override
    public Void visitLogic(Logic logic) {
        Curd leftCurd = logic.getLeftCurd();
        if (leftCurd == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_logic_VALIDATION);
            return null;
        }
        leftCurd.accept(this);
        Curd rightCurd = logic.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitComparison(Comparison comparison) {
        assertIsNot(comparison.getComparisonSymbol(), comparison.getRightCurd());
        Curd leftCurd = comparison.getLeftCurd();
        if (leftCurd == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_comparison_VALIDATION);
            return null;
        }
        leftCurd.accept(this);
        Curd rightCurd = comparison.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    private void assertIsNot(Curd comparisonSymbol, Curd right) {
        if (!(comparisonSymbol instanceof IsNot)) {
            return;
        }

        // comparisonSymbol 是 IsNot，则值必须是 null

        if (!(right instanceof Literal)) {
            error(AstROErrorReporterDelegate.EXPRESSION_isNot_VALIDATION);
            return;
        }

        Token value = ((Literal) right).getValue();

        if (!TokenType.NULL.equals(value.getType())) {
            error(AstROErrorReporterDelegate.EXPRESSION_isNot_VALIDATION);
        }
    }

    @Override
    public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        Curd leftCurd = binaryArithmetic.getLeftCurd();
        if (leftCurd == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_binaryArithmetic_VALIDATION);
            return null;
        }
        leftCurd.accept(this);
        Curd rightCurd = binaryArithmetic.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        boolean bangFlag = false;
        boolean negativeFlag = false;

        UnaryArithmetic copy = unaryArithmetic;

        do {
            Token operator = copy.getOperator();
            if (TokenType.BANG.equals(operator.getType())) {
                bangFlag = true;
            }
            if (TokenType.MINUS.equals(operator.getType())) {
                negativeFlag = true;
            }
            if (bangFlag && negativeFlag) {
                error(AstROErrorReporterDelegate.EXPRESSION_unaryArithmetic_VALIDATION);
                break;
            }
            Curd curd = copy.getCurd();
            if (curd instanceof UnaryArithmetic) {
                copy = (UnaryArithmetic) curd;
            } else {
                break;
            }
        } while (true);
        return null;
    }

    @Override
    public Void visitLiteral(Literal literal) {
        return null;
    }

    @Override
    public Void visitGrouping(Grouping grouping) {
        Curd curd = grouping.getCurd();
        if (curd == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_grouping_VALIDATION);
            return null;
        }
        return curd.accept(this);
    }

    @Override
    public Void visitIdentifier(Identifier identifier) {
        Token name = identifier.getName();
        if (TokenType.IDENTIFIER.equals(name.getType())) {
            String literal = String.valueOf(name.getLiteral());
            if (!literal.matches("[a-zA-Z_\\d*]+\\.?[a-zA-Z_\\d*]*")) {
                error(AstROErrorReporterDelegate.EXPRESSION_identifierName_VALIDATION);
            }
        }
        return null;
    }

    @Override
    public Void visitFunction(Function function) {
        assertFunctionParameter(function);
        return null;
    }

    private void assertFunctionParameter(Curd curd) {
        if (curd instanceof Logic) {
            Logic logic = (Logic) curd;
            assertFunctionParameter(logic.getLeftCurd());
            assertFunctionParameter(logic.getRightCurd());
        } else if (curd instanceof Comparison) {
            Comparison comparison = (Comparison) curd;
            assertFunctionParameter(comparison.getLeftCurd());
            assertFunctionParameter(comparison.getRightCurd());
        } else if (curd instanceof BinaryArithmetic) {
            BinaryArithmetic binaryArithmetic = (BinaryArithmetic) curd;
            assertFunctionParameter(binaryArithmetic.getLeftCurd());
            assertFunctionParameter(binaryArithmetic.getRightCurd());
        } else if (curd instanceof UnaryArithmetic) {
            UnaryArithmetic unaryArithmetic = (UnaryArithmetic) curd;
            assertFunctionParameter(unaryArithmetic.getCurd());
        } else if (curd instanceof GroupFunction
                || curd instanceof SingleSelect || curd instanceof Grouping) {
            error(AstROErrorReporterDelegate.EXPRESSION_functionParameter_VALIDATION);
        }
    }

    @Override
    public Void visitAssignmentList(AssignmentList assignmentList) {
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        if (entryList.isEmpty()) {
            error(AstROErrorReporterDelegate.EXPRESSION_assignmentList_VALIDATION);
            return null;
        }
        for (AssignmentList.Entry entry : entryList) {
            Token columnName = entry.getColumn();
            if (columnName == null) {
                error(AstROErrorReporterDelegate.EXPRESSION_assignmentList_VALIDATION);
                return null;
            }
            Curd value = entry.getValue();
            if (value == null) {
                error(AstROErrorReporterDelegate.EXPRESSION_assignmentList_VALIDATION);
                return null;
            } else {
                value.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitTimeInterval(TimeInterval timeInterval) {
        assertTimeType(timeInterval.getTimeType(), AstROErrorReporterDelegate.EXPRESSION_timeInterval_VALIDATION);
        return null;
    }

    @Override
    public Void visitTimeUnit(TimeUnit timeUnit) {
        assertTimeType(timeUnit.getTimeType(), AstROErrorReporterDelegate.EXPRESSION_timeUnit_VALIDATION);
        return null;
    }

    @Override
    public Void visitIsNot(IsNot isNot) {
        return null;
    }

    private void assertTimeType(Token token, int errorCode) {
        if (token == null || !TokenType.IDENTIFIER.equals(token.getType())) {
            error(errorCode);
            return;
        }
        String type = (String) token.getLiteral();
        if (type == null || !timeTypeSet.contains(type)) {
            error(errorCode);
        }
    }

    private static final Set<String> timeTypeSet = new HashSet<>();

    static {
        timeTypeSet.add(DateConst.MICROSECOND_STR);
        timeTypeSet.add(DateConst.SECOND_STR);
        timeTypeSet.add(DateConst.MINUTE_STR);
        timeTypeSet.add(DateConst.HOUR_STR);
        timeTypeSet.add(DateConst.DAY_STR);
        timeTypeSet.add(DateConst.WEEK_STR);
        timeTypeSet.add(DateConst.MONTH_STR);
        timeTypeSet.add(DateConst.QUARTER_STR);
        timeTypeSet.add(DateConst.YEAR_STR);
    }

}

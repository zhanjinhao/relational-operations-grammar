package cn.addenda.ro.grammar.ast.expression.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/1/5 17:30
 */
public class ExpressionAstMetaDataDetector extends ExpressionVisitorForDelegation<AstMetaData> {

    private static final ExpressionAstMetaDataDetector defaultInstance = new ExpressionAstMetaDataDetector(null);

    public ExpressionAstMetaDataDetector(CurdVisitor<AstMetaData> client) {
        super(client);
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    // ---------------------------------------------------------
    //  Expression涉及到的字段都先认为是条件列，具体是什么由client决定
    // ---------------------------------------------------------

    public static ExpressionAstMetaDataDetector getDefaultInstance() {
        return defaultInstance;
    }

    @Override
    public AstMetaData visitInCondition(InCondition inCondition) {
        AstMetaData astMetaDataCur = inCondition.getAstMetaData();

        // 条件字段需要加入
        Token identifier = inCondition.getIdentifier();
        astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(identifier.getLiteral()));

        Curd curd = inCondition.getSelect();
        // select 模式
        if (curd != null) {
            AstMetaData accept = curd.accept(this);
            accept.setParent(astMetaDataCur);
            astMetaDataCur.addChild(accept);
            return astMetaDataCur;
        } else {
            final List<Curd> range = inCondition.getRange();
            for (Curd item : range) {
                AstMetaData accept = item.accept(this);
                astMetaDataCur.mergeCount(accept);
                // range 模式下，range里的不会包含Identifier，不需要合并ConditionColumn
            }
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitWhereSeg(WhereSeg whereSeg) {
        AstMetaData astMetaDataCur = whereSeg.getAstMetaData();
        Curd logic = whereSeg.getLogic();
        astMetaDataCur.mergeColumnReference(logic.accept(this));
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLogic(Logic logic) {
        return visitBinary(logic);
    }

    @Override
    public AstMetaData visitComparison(Comparison comparison) {
        return visitBinary(comparison);
    }

    @Override
    public AstMetaData visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic);
    }

    private AstMetaData visitBinary(Binary binary) {
        AstMetaData astMetaDataCur = binary.getAstMetaData();

        Curd leftCurd = binary.getLeftCurd();
        astMetaDataCur.mergeColumnReference(leftCurd.accept(this));

        Curd rightCurd = binary.getRightCurd();
        if (rightCurd != null) {
            astMetaDataCur.mergeColumnReference(rightCurd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        AstMetaData astMetaDataCur = unaryArithmetic.getAstMetaData();
        astMetaDataCur.mergeColumnReference(unaryArithmetic.getCurd().accept(this));
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLiteral(Literal literal) {
        final AstMetaData astMetaDataCur = literal.getAstMetaData();
        if (TokenType.PARAMETER.equals(literal.getValue().getType())) {
            astMetaDataCur.incrementParameterCount();
        }
        if (TokenType.HASH_MARK_PLACEHOLDER.equals(literal.getValue().getType())) {
            astMetaDataCur.incrementHashMarkCount();
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitGrouping(Grouping grouping) {
        AstMetaData astMetaDataCur = grouping.getAstMetaData();

        AstMetaData astMetaData = grouping.getCurd().accept(this);
        astMetaDataCur.mergeColumnReference(astMetaData);
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitIdentifier(Identifier identifier) {
        AstMetaData astMetaDataCur = identifier.getAstMetaData();

        Token value = identifier.getName();
        if (TokenType.IDENTIFIER.equals(value.getType()) || TokenType.STAR.equals(value.getType())) {
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(value.getLiteral()));
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitFunction(Function function) {
        AstMetaData astMetaDataCur = function.getAstMetaData();
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            for (Curd curd : parameterList) {
                astMetaDataCur.mergeColumnReference(curd.accept(this));
            }
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        AstMetaData astMetaDataCur = assignmentList.getAstMetaData();
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            Token columnName = entry.getColumn();
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(columnName.getLiteral()));
            astMetaDataCur.mergeColumnReference(entry.getValue().accept(this));
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitTimeInterval(TimeInterval timeInterval) {
        return timeInterval.getAstMetaData();
    }

    @Override
    public AstMetaData visitTimeUnit(TimeUnit timeUnit) {
        return timeUnit.getAstMetaData();
    }

    @Override
    public AstMetaData visitIsNot(IsNot isNot) {
        return isNot.getAstMetaData();
    }

}

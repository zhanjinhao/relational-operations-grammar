package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionVisitorForDelegation;
import cn.addenda.ro.grammar.ast.retrieve.Select;

/**
 * @author addenda
 * @datetime 2022/1/13 22:35
 */
public class ExpressionAstMetaDataDetectorWrapperForRetrieve extends ExpressionVisitorForDelegation<AstMetaData> {

    private final ExpressionAstMetaDataDetector expressionAstMetaDataDetector;

    public ExpressionAstMetaDataDetectorWrapperForRetrieve(CurdVisitor<AstMetaData> client, ROErrorReporter roErrorReporter) {
        super(client);
        setErrorReporter(roErrorReporter);
        expressionAstMetaDataDetector = new ExpressionAstMetaDataDetector(client, roErrorReporter);
    }

    @Override
    public AstMetaData visitWhereSeg(WhereSeg whereSeg) {
        Curd curd = whereSeg.getLogic();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = whereSeg.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.addChild(accept);
            accept.setParent(astMetaDataCur);
            return astMetaDataCur;
        } else {
            return expressionAstMetaDataDetector.visitWhereSeg(whereSeg);
        }
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

    @Override
    public AstMetaData visitBinary(Binary binary) {
        AstMetaData astMetaDataCur = binary.getAstMetaData();

        Curd leftCurd = binary.getLeftCurd();
        AstMetaData leftAccept = leftCurd.accept(this);
        if (leftCurd instanceof Select) {
            astMetaDataCur.addChild(leftAccept);
            leftAccept.setParent(astMetaDataCur);
        } else {
            astMetaDataCur.mergeColumnReference(leftAccept);
        }

        Curd rightCurd = binary.getRightCurd();
        if (rightCurd != null) {
            AstMetaData rightAccept = rightCurd.accept(this);
            if (rightCurd instanceof Select) {
                astMetaDataCur.addChild(rightAccept);
                rightAccept.setParent(astMetaDataCur);
            } else {
                astMetaDataCur.mergeColumnReference(rightAccept);
            }
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        Curd curd = unaryArithmetic.getCurd();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = unaryArithmetic.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.addChild(accept);
            return astMetaDataCur;
        } else {
            return expressionAstMetaDataDetector.visitUnaryArithmetic(unaryArithmetic);
        }
    }

    @Override
    public AstMetaData visitLiteral(Literal literal) {
        return expressionAstMetaDataDetector.visitLiteral(literal);
    }

    @Override
    public AstMetaData visitGrouping(Grouping grouping) {
        return expressionAstMetaDataDetector.visitGrouping(grouping);
    }

    @Override
    public AstMetaData visitIdentifier(Identifier identifier) {
        return expressionAstMetaDataDetector.visitIdentifier(identifier);
    }

    @Override
    public AstMetaData visitFunction(Function function) {
        return expressionAstMetaDataDetector.visitFunction(function);
    }

    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        return expressionAstMetaDataDetector.visitAssignmentList(assignmentList);
    }

    @Override
    public AstMetaData visitTimeInterval(TimeInterval timeInterval) {
        return expressionAstMetaDataDetector.visitTimeInterval(timeInterval);
    }

    @Override
    public AstMetaData visitTimeUnit(TimeUnit timeUnit) {
        return expressionAstMetaDataDetector.visitTimeUnit(timeUnit);
    }

    @Override
    public AstMetaData visitIsNot(IsNot isNot) {
        return expressionAstMetaDataDetector.visitIsNot(isNot);
    }
}

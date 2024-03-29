package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionVisitorForDelegation;

/**
 * @author addenda
 * @datetime 2021/4/7 18:58
 */
public abstract class InsertVisitorWithDelegate<R> extends InsertVisitor<R> {

    protected ExpressionVisitorForDelegation<R> delegate;

    protected void init(ExpressionVisitorForDelegation<R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return binaryArithmetic.accept(delegate);
    }

    @Override
    public R visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return unaryArithmetic.accept(delegate);
    }

    @Override
    public R visitLiteral(Literal literal) {
        return literal.accept(delegate);
    }

    @Override
    public R visitGrouping(Grouping grouping) {
        return grouping.accept(delegate);
    }

    @Override
    public R visitIdentifier(Identifier identifier) {
        return identifier.accept(delegate);
    }

    @Override
    public R visitFunction(Function function) {
        return function.accept(delegate);
    }

    @Override
    public R visitAssignmentList(AssignmentList assignmentList) {
        return assignmentList.accept(delegate);
    }

    @Override
    public R visitTimeInterval(TimeInterval timeInterval) {
        return timeInterval.accept(delegate);
    }

    @Override
    public R visitTimeUnit(TimeUnit timeUnit) {
        return timeUnit.accept(delegate);
    }

    @Override
    public R visitIsNot(IsNot isNot) {
        return isNot.accept(delegate);
    }
}

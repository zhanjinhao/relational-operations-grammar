package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.grammar.ast.AbstractCurdVisitor;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.Comparison;
import cn.addenda.ro.grammar.ast.expression.Logic;
import cn.addenda.ro.grammar.ast.expression.WhereSeg;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 *
 * 所有的非insert语法抛异常。
 *
 * @author addenda
 * @datetime 2021/4/3 19:14
 */
public abstract class InsertVisitor<R> extends AbstractCurdVisitor<R> {

    @Override
    public final R visitSelect(Select select) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitSingleSelect(SingleSelect singleSelect) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitColumnSeg(ColumnSeg columnSeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitColumnRep(ColumnRep columnRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitCaseWhen(CaseWhen caseWhen) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitTableSeg(TableSeg tableSeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitTableRep(TableRep tableRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitWhereSeg(WhereSeg whereSeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInCondition(InCondition inCondition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitExistsCondition(ExistsCondition existsCondition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitGroupBySeg(GroupBySeg groupBySeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitOrderBySeg(OrderBySeg orderBySeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitLimitSeg(LimitSeg limitSeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitLogic(Logic logic) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitComparison(Comparison comparison) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitGroupFunction(GroupFunction groupFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitSLock(SLock sLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitXLock(XLock xLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitUpdate(Update update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitDelete(Delete delete) {
        throw new UnsupportedOperationException();
    }
}



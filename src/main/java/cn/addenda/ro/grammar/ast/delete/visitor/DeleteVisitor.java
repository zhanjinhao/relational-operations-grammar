package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.grammar.ast.AbstractCurdVisitor;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * @author addenda
 * @datetime 2021/4/8 20:45
 */
public abstract class DeleteVisitor<R> extends AbstractCurdVisitor<R> {

    @Override
    public final R visitInsertSelectRep(InsertSelectRep insertSelectRep) {
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
    public final R visitCaseWhen(CaseWhen caseWhen) {
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
    public final R visitOrderItem(OrderItem orderItem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitSLock(SLock sLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitXLock(XLock xLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitFrameEdge(FrameEdge frameEdge) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitFrameBetween(FrameBetween frameBetween) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitDynamicFrame(DynamicFrame dynamicFrame) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitWindow(Window window) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitWindowFunction(WindowFunction windowFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitLimitSeg(LimitSeg limitSeg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitGroupFunction(GroupFunction groupFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitGroupConcat(GroupConcat groupConcat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsert(Insert insert) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsertSetRep(InsertSetRep insertSetRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitUpdate(Update update) {
        throw new UnsupportedOperationException();
    }

}

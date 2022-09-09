package cn.addenda.ro.grammar.ast.expression.visitor;

import cn.addenda.ro.grammar.ast.AbstractCurdVisitor;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * 此类本质是一个delegate
 *
 * @author addenda
 * @datetime 2021/4/7 18:07
 */
public abstract class ExpressionVisitorForDelegation<R> extends AbstractCurdVisitor<R> {

    /**
     * ExpressionVisitorForDelegation 作为一个委托存在，可能会递归到非expression语法，
     * 所以对于非expression语法，需要需要用client去visit语法。
     */
    protected CurdVisitor<R> client;

    protected ExpressionVisitorForDelegation(CurdVisitor<R> client) {
        super();
        this.client = client;
    }

    @Override
    public final R visitSelect(Select select) {
        return select.accept(client);
    }

    @Override
    public final R visitSingleSelect(SingleSelect singleSelect) {
        return singleSelect.accept(client);
    }

    @Override
    public final R visitColumnSeg(ColumnSeg columnSeg) {
        return columnSeg.accept(client);
    }

    @Override
    public final R visitColumnRep(ColumnRep columnRep) {
        return columnRep.accept(client);
    }

    @Override
    public final R visitCaseWhen(CaseWhen caseWhen) {
        return caseWhen.accept(client);
    }

    @Override
    public final R visitTableSeg(TableSeg tableSeg) {
        return tableSeg.accept(client);
    }

    @Override
    public final R visitTableRep(TableRep tableRep) {
        return tableRep.accept(client);
    }

    @Override
    public final R visitExistsCondition(ExistsCondition existsCondition) {
        return existsCondition.accept(client);
    }

    @Override
    public final R visitGroupBySeg(GroupBySeg groupBySeg) {
        return groupBySeg.accept(client);
    }

    @Override
    public final R visitOrderBySeg(OrderBySeg orderBySeg) {
        return orderBySeg.accept(client);
    }

    @Override
    public final R visitOrderItem(OrderItem orderItem) {
        return orderItem.accept(client);
    }

    @Override
    public final R visitLimitSeg(LimitSeg limitSeg) {
        return limitSeg.accept(client);
    }

    @Override
    public final R visitGroupFunction(GroupFunction groupFunction) {
        return groupFunction.accept(client);
    }

    @Override
    public final R visitGroupConcat(GroupConcat groupConcat) {
        return groupConcat.accept(client);
    }

    @Override
    public final R visitInsert(Insert insert) {
        return insert.accept(client);
    }

    @Override
    public final R visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        return insertValuesRep.accept(client);
    }

    @Override
    public final R visitInsertSetRep(InsertSetRep insertSetRep) {
        return insertSetRep.accept(client);
    }

    @Override
    public final R visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        return insertSelectRep.accept(client);
    }

    @Override
    public final R visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        return onDuplicateKey.accept(client);
    }

    @Override
    public final R visitSLock(SLock sLock) {
        return sLock.accept(client);
    }

    @Override
    public final R visitXLock(XLock xLock) {
        return xLock.accept(client);
    }

    @Override
    public final R visitFrameEdge(FrameEdge frameEdge) {
        return frameEdge.accept(client);
    }

    @Override
    public final R visitFrameBetween(FrameBetween frameBetween) {
        return frameBetween.accept(client);
    }

    @Override
    public final R visitDynamicFrame(DynamicFrame dynamicFrame) {
        return dynamicFrame.accept(client);
    }

    @Override
    public final R visitWindow(Window window) {
        return window.accept(client);
    }

    @Override
    public final R visitWindowFunction(WindowFunction windowFunction) {
        return windowFunction.accept(client);
    }

    @Override
    public final R visitUpdate(Update update) {
        return update.accept(client);
    }

    @Override
    public final R visitDelete(Delete delete) {
        return delete.accept(client);
    }
}

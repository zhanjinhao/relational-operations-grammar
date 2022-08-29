package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class SingleSelect extends Curd {

    private SingleSelectType singleSelectType = SingleSelectType.UNDETERMINED;

    private Curd columnSeg;

    private Curd tableSeg;

    private Curd whereSeg;

    private Curd groupBySeg;

    private Curd orderBySeg;

    private Curd limitSeg;

    private Curd lockSeg;

    public SingleSelect(Curd columnSeg, Curd tableSeg, Curd whereSeg,
        Curd groupBySeg, Curd orderBySeg, Curd limitSeg, Curd lockSeg) {
        super(new SingleSelectAstMetaData());
        this.columnSeg = columnSeg;
        this.tableSeg = tableSeg;
        this.whereSeg = whereSeg;
        this.groupBySeg = groupBySeg;
        this.orderBySeg = orderBySeg;
        this.limitSeg = limitSeg;
        this.lockSeg = lockSeg;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitSingleSelect(this);
    }

    public SingleSelectType getSingleSelectType() {
        return singleSelectType;
    }

    public void setSingleSelectType(SingleSelectType singleSelectType) {
        this.singleSelectType = singleSelectType;
    }

    public Curd getColumnSeg() {
        return columnSeg;
    }

    public Curd getTableSeg() {
        return tableSeg;
    }

    public Curd getWhereSeg() {
        return whereSeg;
    }

    public Curd getGroupBySeg() {
        return groupBySeg;
    }

    public Curd getOrderBySeg() {
        return orderBySeg;
    }

    public Curd getLimitSeg() {
        return limitSeg;
    }

    public Curd getLockSeg() {
        return lockSeg;
    }
}

package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/31 19:17
 */
public class Window extends Curd {

    private List<Curd> partitionByList;

    private Curd orderBySeg;

    private Curd dynamicFrame;

    public Window(List<Curd> partitionByList, Curd orderBySeg, Curd dynamicFrame) {
        super(new SingleSelectAstMetaData());
        this.partitionByList = partitionByList;
        this.orderBySeg = orderBySeg;
        this.dynamicFrame = dynamicFrame;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitWindow(this);
    }

    public List<Curd> getPartitionByList() {
        return partitionByList;
    }

    public Curd getOrderBySeg() {
        return orderBySeg;
    }

    public Curd getDynamicFrame() {
        return dynamicFrame;
    }
}

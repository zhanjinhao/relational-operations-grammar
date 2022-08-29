package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class OrderBySeg extends Curd {

    private List<Curd> columnList;

    public OrderBySeg(List<Curd> columnList) {
        super(new SingleSelectAstMetaData());
        this.columnList = columnList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOrderBySeg(this);
    }

    public List<Curd> getColumnList() {
        return columnList;
    }

}

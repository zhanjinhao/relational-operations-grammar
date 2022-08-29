package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/08/29
 */
public class OrderItem extends Curd {

    private Curd column;
    private Token orderType;

    public OrderItem(Curd column, Token orderType) {
        super(new SingleSelectAstMetaData());
        this.column = column;
        this.orderType = orderType;
    }

    public OrderItem(Curd column) {
        this.column = column;
        orderType = null;
    }

    public Curd getColumn() {
        return column;
    }

    public Token getOrderType() {
        return orderType;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOrderItem(this);
    }
}
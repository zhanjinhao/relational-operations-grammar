package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class OrderBySeg extends Curd {

    private List<OrderItem> columnList;

    public OrderBySeg(List<OrderItem> columnList) {
        this.columnList = columnList;
    }

    public List<OrderItem> getColumnList() {
        return columnList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOrderBySeg(this);
    }

    public static class OrderItem {

        private Token column;
        private Token orderType;

        public OrderItem(Token column, Token orderType) {
            this.column = column;
            this.orderType = orderType;
        }

        public OrderItem(Token column) {
            this.column = column;
            orderType = null;
        }

        public Token getColumn() {
            return column;
        }

        public Token getOrderType() {
            return orderType;
        }
    }
}

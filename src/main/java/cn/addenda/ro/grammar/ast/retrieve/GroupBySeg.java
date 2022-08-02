package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class GroupBySeg extends Curd {

    private List<Token> columnList;

    private Curd having;

    public GroupBySeg(List<Token> columnList, Curd having) {
        this.columnList = columnList;
        this.having = having;
    }

    public GroupBySeg(List<Token> columnList) {
        this.columnList = columnList;
        this.having = null;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitGroupBySeg(this);
    }

    public List<Token> getColumnList() {
        return columnList;
    }

    public Curd getHaving() {
        return having;
    }
}

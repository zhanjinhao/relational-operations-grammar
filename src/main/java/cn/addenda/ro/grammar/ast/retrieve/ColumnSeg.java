package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class ColumnSeg extends Curd {

    private Token restriction;

    private List<Curd> columnRepList;

    public ColumnSeg(Token restriction, List<Curd> columnRepList) {
        super(new SingleSelectAstMetaData());
        this.restriction = restriction;
        this.columnRepList = columnRepList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitColumnSeg(this);
    }

    public Token getRestriction() {
        return restriction;
    }

    public List<Curd> getColumnRepList() {
        return columnRepList;
    }
}

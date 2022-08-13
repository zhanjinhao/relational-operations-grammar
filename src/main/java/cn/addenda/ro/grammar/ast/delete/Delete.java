package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/4/8 20:38
 */
public class Delete extends Curd {

    private Token tableName;

    private Curd whereSeg;

    public Delete(Token tableName, Curd whereSeg) {
        super(new DeleteAstMetaData());
        this.tableName = tableName;
        this.whereSeg = whereSeg;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitDelete(this);
    }

    public Token getTableName() {
        return tableName;
    }

    public Curd getWhereSeg() {
        return whereSeg;
    }

}

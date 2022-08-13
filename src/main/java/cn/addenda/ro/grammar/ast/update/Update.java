package cn.addenda.ro.grammar.ast.update;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/4/6
 */
public class Update extends Curd {

    private Token tableName;

    private Curd assignmentList;

    private Curd whereSeg;

    public Update(Token tableName, Curd assignmentList, Curd whereSeg) {
        super(new UpdateAstMetaData());
        this.tableName = tableName;
        this.assignmentList = assignmentList;
        this.whereSeg = whereSeg;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitUpdate(this);
    }

    public Token getTableName() {
        return tableName;
    }

    public Curd getAssignmentList() {
        return assignmentList;
    }

    public Curd getWhereSeg() {
        return whereSeg;
    }

}

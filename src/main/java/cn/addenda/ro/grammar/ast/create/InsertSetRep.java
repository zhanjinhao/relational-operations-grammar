package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @datetime 2021/4/5 21:12
 */
public class InsertSetRep extends Curd {

    private Curd assignmentList;

    public InsertSetRep(Curd assignmentList) {
        super(new InsertAstMetaData());
        this.assignmentList = assignmentList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsertSetRep(this);
    }

    public Curd getAssignmentList() {
        return assignmentList;
    }

}

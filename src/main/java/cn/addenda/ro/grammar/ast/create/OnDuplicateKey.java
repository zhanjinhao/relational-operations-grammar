package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @date 2021/4/6
 */
public class OnDuplicateKey extends Curd {

    private Curd assignmentList;

    public OnDuplicateKey(Curd assignmentList) {
        super(new InsertAstMetaData());
        this.assignmentList = assignmentList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOnDuplicateKey(this);
    }

    public Curd getAssignmentList() {
        return assignmentList;
    }
}

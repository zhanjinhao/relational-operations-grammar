package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.grammar.ast.AbstractCurdVisitor;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.AssignmentList;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * @author addenda
 * @datetime 2021/4/5 20:52
 */
public abstract class SelectVisitor<R> extends AbstractCurdVisitor<R> {

    @Override
    public final R visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsert(Insert insert) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitInsertSetRep(InsertSetRep insertSetRep) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitAssignmentList(AssignmentList assignmentList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final R visitUpdate(Update update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R visitDelete(Delete delete) {
        throw new UnsupportedOperationException();
    }
}

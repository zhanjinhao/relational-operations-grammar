package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/4/5 21:00
 */
public class Insert extends Curd {

    private InsertType insertType;

    // ignore?
    private Token constrict;

    private Curd onDuplicateUpdate;

    private Token tableName;

    // insertValuesRep | insertSetRep | insertSelectRep
    private Curd curd;

    public Insert(Token constrict, Token tableName, Curd curd, InsertType insertType) {
        this.constrict = constrict;
        this.tableName = tableName;
        this.curd = curd;
        this.insertType = insertType;
        onDuplicateUpdate = null;
    }

    public Insert(Token constrict, Token tableName, Curd curd, Curd onDuplicateUpdate, InsertType insertType) {
        this.constrict = constrict;
        this.tableName = tableName;
        this.curd = curd;
        this.onDuplicateUpdate = onDuplicateUpdate;
        this.insertType = insertType;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsert(this);
    }

    public Token getConstrict() {
        return constrict;
    }

    public Curd getOnDuplicateUpdate() {
        return onDuplicateUpdate;
    }

    public Token getTableName() {
        return tableName;
    }

    public Curd getCurd() {
        return curd;
    }

    public InsertType getInsertType() {
        return insertType;
    }

}

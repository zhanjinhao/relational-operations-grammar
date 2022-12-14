package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class TableRep extends Curd {

    private Curd curd;
    private Token alias;

    /**
     * alias 是别名，curd 是真实表
     */
    public TableRep(Curd curd, Token alias) {
        super(new SingleSelectAstMetaData());
        this.curd = curd;
        this.alias = alias;
    }

    /**
     * curd 是真实表
     */
    public TableRep(Curd curd) {
        super(new SingleSelectAstMetaData());
        this.curd = curd;
    }


    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTableRep(this);
    }

    public Curd getCurd() {
        return curd;
    }

    public Token getAlias() {
        return alias;
    }
}

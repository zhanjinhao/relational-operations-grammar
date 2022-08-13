package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/3 22:30
 */
public class GroupFunction extends Curd {

    private Token method;

    private Curd curd;

    public GroupFunction(Token method, Curd curd) {
        super(new SingleSelectAstMetaData());
        this.method = method;
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitGroupFunction(this);
    }

    public Token getMethod() {
        return method;
    }

    public Curd getCurd() {
        return curd;
    }
}

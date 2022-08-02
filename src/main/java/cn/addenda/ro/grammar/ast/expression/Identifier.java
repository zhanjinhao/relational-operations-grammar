package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/2/22 19:02
 */
public class Identifier extends Curd {

    public Identifier(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitIdentifier(this);
    }

    private Token name;

    public Token getName() {
        return name;
    }
}
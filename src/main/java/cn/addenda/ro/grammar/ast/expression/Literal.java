package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/2/20 21:33
 */
public class Literal extends Curd {

    public Literal(Token value) {
        this.value = value;
    }

    @Override
    public <R> R accept(CurdVisitor<R> expressionVisitor) {
        return expressionVisitor.visitLiteral(this);
    }

    private Token value;

    public Token getValue() {
        return value;
    }
}
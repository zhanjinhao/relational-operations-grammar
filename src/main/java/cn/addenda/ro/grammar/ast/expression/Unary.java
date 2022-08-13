package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/2 19:56
 */
public abstract class Unary extends Curd {

    private Token operator;
    private Curd curd;

    protected Unary(Token operator, Curd curd) {
        this.operator = operator;
        this.curd = curd;
    }

    protected Unary(Token operator, Curd curd, AstMetaData astMetaData) {
        super(astMetaData);
        this.operator = operator;
        this.curd = curd;
    }

    public Token getOperator() {
        return operator;
    }

    public Curd getCurd() {
        return curd;
    }

}

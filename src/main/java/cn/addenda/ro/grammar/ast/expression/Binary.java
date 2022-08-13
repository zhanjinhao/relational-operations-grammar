package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/2 19:53
 */
public abstract class Binary extends Curd {

    private Curd leftCurd;

    private Token token;

    private Curd rightCurd;

    protected Binary(Curd leftCurd, Token token, Curd rightCurd) {
        this.leftCurd = leftCurd;
        this.token = token;
        this.rightCurd = rightCurd;
    }

    protected Binary(Curd leftCurd, Token token, Curd rightCurd, AstMetaData astMetaData) {
        super(astMetaData);
        this.leftCurd = leftCurd;
        this.token = token;
        this.rightCurd = rightCurd;
    }

    public Curd getLeftCurd() {
        return leftCurd;
    }

    public Token getToken() {
        return token;
    }

    public Curd getRightCurd() {
        return rightCurd;
    }
}

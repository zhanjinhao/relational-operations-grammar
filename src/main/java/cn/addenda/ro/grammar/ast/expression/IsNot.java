package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/12/22 16:44
 */
public class IsNot extends Curd {

    private Token isToken;

    private Token notToken;

    public IsNot(Token isToken, Token notToken) {
        this.isToken = isToken;
        this.notToken = notToken;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitIsNot(this);
    }

    public Token getIsToken() {
        return isToken;
    }


    public Token getNotToken() {
        return notToken;
    }

}

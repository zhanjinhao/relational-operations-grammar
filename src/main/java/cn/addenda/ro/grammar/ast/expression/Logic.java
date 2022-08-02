package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class Logic extends Binary {

    public Logic(Curd leftCurd, Token token,
                 Curd rightCurd) {
        super(leftCurd, token, rightCurd);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitLogic(this);
    }

}

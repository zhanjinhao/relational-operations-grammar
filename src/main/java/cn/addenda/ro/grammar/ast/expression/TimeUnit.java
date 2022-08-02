package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/8/15 16:16
 */
public class TimeUnit extends Curd {

    private Curd curd;

    private Token timeType;

    public TimeUnit(Token timeType, Curd curd) {
        this.timeType = timeType;
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTimeUnit(this);
    }

    public Curd getCurd() {
        return curd;
    }

    public Token getTimeType() {
        return timeType;
    }
}

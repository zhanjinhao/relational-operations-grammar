package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.math.BigInteger;

/**
 * @author addenda
 * @datetime 2021/7/26 23:16
 */
public class TimeInterval extends Curd {

    private Token timeType;

    private BigInteger interval;

    public TimeInterval(Token timeType, BigInteger interval) {
        this.timeType = timeType;
        this.interval = interval;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTimeInterval(this);
    }

    public Token getTimeType() {
        return timeType;
    }

    public BigInteger getInterval() {
        return interval;
    }

}

package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/7/26 23:16
 */
public class TimeInterval extends Curd {

    private Token timeType;

    private Long interval;

    public TimeInterval(Token timeType, Long interval) {
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

    public Long getInterval() {
        return interval;
    }

}

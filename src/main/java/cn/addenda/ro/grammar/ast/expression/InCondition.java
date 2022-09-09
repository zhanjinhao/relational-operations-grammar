package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class InCondition extends Curd {

    // in or not
    private Token in;

    private Token identifier;

    // curd list
    private List<Curd> range;

    // select
    private Curd select;

    // select 场景
    public InCondition(Token in, Token identifier, Curd select) {
        this.in = in;
        this.identifier = identifier;
        this.select = select;
    }

    // list 场景
    public InCondition(Token in, Token identifier, List<Curd> range) {
        this.in = in;
        this.identifier = identifier;
        this.range = range;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInCondition(this);
    }

    public Token getIn() {
        return in;
    }

    public Token getIdentifier() {
        return identifier;
    }

    public List<Curd> getRange() {
        return range;
    }

    public Curd getSelect() {
        return select;
    }
}

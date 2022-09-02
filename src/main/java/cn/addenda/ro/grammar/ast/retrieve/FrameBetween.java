package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @datetime 2022/8/31 19:11
 */
public class FrameBetween extends Curd {

    private Curd from;
    private Curd to;

    public FrameBetween(Curd from, Curd to) {
        super(new SingleSelectAstMetaData());
        this.from = from;
        this.to = to;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitFrameBetween(this);
    }

    public Curd getFrom() {
        return from;
    }

    public Curd getTo() {
        return to;
    }
}

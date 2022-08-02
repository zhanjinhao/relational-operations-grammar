package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class WhereSeg extends Curd {

    private Curd logic;

    public WhereSeg(Curd logic) {
        this.logic = logic;
    }

    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitWhereSeg(this);
    }

    public Curd getLogic() {
        return logic;
    }
}

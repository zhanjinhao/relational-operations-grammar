package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @date 2021/4/6
 */
public class OnDuplicateKey extends Curd {

    private Curd curd;

    public OnDuplicateKey(Curd curd) {
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOnDuplicateKey(this);
    }

    public Curd getCurd() {
        return curd;
    }
}

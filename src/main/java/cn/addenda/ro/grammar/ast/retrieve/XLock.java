package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @author addenda
 * @datetime 2022/8/29 14:15
 */
public class XLock extends Curd {

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitXLock(this);
    }

}

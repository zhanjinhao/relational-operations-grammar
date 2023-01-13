package cn.addenda.ro.grammar.function.descriptor;

import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;

/**
 * @author addenda
 * @datetime 2021/7/24 22:58
 */
public interface FunctionDescriptor {

    boolean isIndependent();

    String functionName();

    int innerType();

    void staticCheck(Function function, CurdType type);

}

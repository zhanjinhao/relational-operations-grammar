package cn.addenda.ro.grammar.function.descriptor.string;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

/**
 * @author addenda
 * @datetime 2021/7/28 19:13
 */
public class ConcatDescriptor extends AbstractFunctionDescriptor {

    public ConcatDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "concat";
    }

    @Override
    public int innerType() {
        return DataTypeConst.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        for (Curd curd : function.getParameterList()) {
            checkType(curd, function, Literal.class, Identifier.class);
        }
    }

}

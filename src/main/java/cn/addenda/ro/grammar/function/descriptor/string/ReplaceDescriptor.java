package cn.addenda.ro.grammar.function.descriptor.string;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/28 19:11
 */
public class ReplaceDescriptor extends AbstractFunctionDescriptor {

    public ReplaceDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    @Override
    public String functionName() {
        return "replace";
    }

    @Override
    public int innerType() {
        return DataTypeConst.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 3);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Literal.class, Identifier.class);
        checkType(parameterList.get(1), function, Literal.class);
        checkType(parameterList.get(2), function, Literal.class);
    }

}

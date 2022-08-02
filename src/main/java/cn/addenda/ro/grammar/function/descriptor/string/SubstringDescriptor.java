package cn.addenda.ro.grammar.function.descriptor.string;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/28 19:12
 */
public class SubstringDescriptor extends AbstractFunctionDescriptor {

    public SubstringDescriptor(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "substring";
    }

    @Override
    public int innerType() {
        return DataTypeConst.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        for (Curd curd : parameterList) {
            checkType(curd, function, Literal.class, UnaryArithmetic.class);
        }
    }

}

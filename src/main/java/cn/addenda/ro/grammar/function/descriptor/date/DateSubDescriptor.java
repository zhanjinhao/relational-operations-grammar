package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

/**
 * @author addenda
 * @datetime 2021/7/27 21:46
 */
public class DateSubDescriptor extends AbstractFunctionDescriptor {

    public DateSubDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "date_sub";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        FunctionDescriptor dateAddFunction = functionEvaluator.getFunction("date_add");
        dateAddFunction.staticCheck(function, type);
    }

}

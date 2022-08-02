package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/4/11 15:38
 */
public class DateFormatDescriptor extends AbstractFunctionDescriptor {

    public DateFormatDescriptor(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "date_format";
    }

    @Override
    public int innerType() {
        return DataTypeConst.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 2);
        List<Curd> parameterList = function.getParameterList();
        checkDate(parameterList.get(0), function);
        checkType(parameterList.get(1), function, Literal.class, BinaryArithmetic.class);
    }

}

package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/27 23:23
 */
public class TimestampDiffDescriptor extends AbstractFunctionDescriptor {

    public TimestampDiffDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    @Override
    public String functionName() {
        return "timestampdiff";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 3);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Identifier.class);
        checkDate(parameterList.get(1), function);
        checkDate(parameterList.get(2), function);

        parameterList.set(0, new Attachment(parameterList.get(0)));

    }

}

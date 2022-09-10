package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/31 22:59
 */
public class DateDescriptor extends AbstractFunctionDescriptor {

    public DateDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    @Override
    public String functionName() {
        return "date";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkDate(parameterList.get(0), function);
    }

}

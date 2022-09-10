package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.expression.TimeInterval;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/27 21:46
 */
public class DateAddDescriptor extends AbstractFunctionDescriptor {

    public DateAddDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    @Override
    public String functionName() {
        return "date_add";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 2);
        List<Curd> parameterList = function.getParameterList();
        checkDate(parameterList.get(0), function);
        checkType(parameterList.get(1), function, TimeInterval.class);
    }

}

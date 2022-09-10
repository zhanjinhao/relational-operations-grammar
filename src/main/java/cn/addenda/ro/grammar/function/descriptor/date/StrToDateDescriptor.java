package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.expression.Literal;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * 字符串转日期
 *
 * @author addenda
 * @datetime 2021/4/11 15:49
 */
public class StrToDateDescriptor extends AbstractFunctionDescriptor {

    public StrToDateDescriptor(FunctionEvaluator<?> functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    @Override
    public String functionName() {
        return "str_to_date";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 2);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Literal.class);
        checkType(parameterList.get(1), function, Literal.class);
    }

}

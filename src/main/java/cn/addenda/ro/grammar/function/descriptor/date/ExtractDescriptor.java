package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.expression.TimeUnit;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/8/15 20:33
 */
public class ExtractDescriptor extends AbstractFunctionDescriptor {

    public ExtractDescriptor(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "extract";
    }

    @Override
    public int innerType() {
        return DataTypeConst.INTEGER;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, TimeUnit.class);
    }

}

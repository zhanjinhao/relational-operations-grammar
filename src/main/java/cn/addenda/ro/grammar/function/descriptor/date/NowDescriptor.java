package cn.addenda.ro.grammar.function.descriptor.date;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.ErrorReportableFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionROErrorReporterDelegate;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/4/11 14:56
 */
public class NowDescriptor extends ErrorReportableFunctionDescriptor {

    @Override
    public String functionName() {
        return "now";
    }

    @Override
    public int innerType() {
        return DataTypeConst.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null || !parameterList.isEmpty()) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, function);
            return;
        }
    }
}

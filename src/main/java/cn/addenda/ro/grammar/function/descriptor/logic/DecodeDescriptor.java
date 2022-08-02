package cn.addenda.ro.grammar.function.descriptor.logic;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.ErrorReportableFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionROErrorReporterDelegate;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/31 8:45
 */
public class DecodeDescriptor extends ErrorReportableFunctionDescriptor {

    @Override
    public String functionName() {
        return "decode";
    }

    @Override
    public int innerType() {
        return DataTypeConst.ALL;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList == null) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE);
            return;
        }
        if (parameterList.size() % 2 == 1) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE);
            return;
        }
    }

}

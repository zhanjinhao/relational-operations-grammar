package cn.addenda.ro.grammar.function.descriptor;

import cn.addenda.ro.data.DataTypeConst;
import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.expression.Identifier;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/7/29 13:56
 */
public abstract class AbstractFunctionDescriptor extends ErrorReportableFunctionDescriptor {

    protected FunctionEvaluator functionEvaluator;

    protected AbstractFunctionDescriptor(FunctionEvaluator functionEvaluator) {
        this.functionEvaluator = functionEvaluator;
    }

    protected int checkParameterSize(Function function, int... sizes) {
        List<Curd> parameterList = function.getParameterList();
        for (int size : sizes) {
            if (parameterList != null && parameterList.size() == size) {
                return size;
            }
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, function);
        return -1;
    }

    protected void checkDate(Curd date, ROError attachment) {
        if (date instanceof Identifier) {
            return;
        }
        if (!(date instanceof Function)) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, attachment);
            return;
        }

        Function dateFunction = (Function) date;
        FunctionDescriptor functionDescriptor = functionEvaluator.getFunctionDescriptor((String) dateFunction.getMethod().getLiteral());
        if (functionDescriptor.innerType() != DataTypeConst.DATE) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, attachment);
        }
    }

    protected void checkType(Curd curd, ROError attachment, Class<?>... clazz) {

        int length = clazz.length;
        if (length < 1) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, attachment);
            return;
        }

        for (Class<?> aClass : clazz) {
            if ((aClass.isAssignableFrom(curd.getClass()))) {
                return;
            }
        }

        error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, attachment);
    }

}

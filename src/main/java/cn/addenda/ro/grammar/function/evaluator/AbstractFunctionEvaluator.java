package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionROErrorReporterDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/7/24 23:21
 */
public abstract class AbstractFunctionEvaluator implements FunctionEvaluator, ROErrorReporter {

    private final HashMap<String, FunctionDescriptor> functionDescriptorMap = new HashMap<>();

    private final ROErrorReporter errorReporterDelegate = new FunctionROErrorReporterDelegate();

    @Override
    public void addFunctionDescriptor(Class<? extends FunctionDescriptor> functionHandlerClass) {
        if (functionHandlerClass == null) {
            return;
        }
        try {
            FunctionDescriptor functionDescriptor;
            // 对于AbstractFunctionHandler来说，需要调用带FunctionEvaluator参数的构造方法
            if (AbstractFunctionDescriptor.class.isAssignableFrom(functionHandlerClass)) {
                Constructor<? extends FunctionDescriptor> constructor = functionHandlerClass.getConstructor(FunctionEvaluator.class);
                functionDescriptor = constructor.newInstance(this);
            } else {
                functionDescriptor = functionHandlerClass.newInstance();
            }
            if (functionDescriptorMap.containsKey(functionDescriptor.functionName())) {
                error(FunctionROErrorReporterDelegate.FUNCTION_HANDLER_REPEATED_PARSE);
                return;
            }
            functionDescriptorMap.put(functionDescriptor.functionName(), functionDescriptor);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            error(FunctionROErrorReporterDelegate.FUNCTION_HANDLER_INSTANTIATION_PARSE);
        }
    }

    @Override
    public FunctionDescriptor getFunctionDescriptor(String functionName) {
        return functionDescriptorMap.get(functionName);
    }

    @Override
    public Set<String> functionNameSet() {
        return functionDescriptorMap.keySet();
    }

    @Override
    public Map<String, Integer> functionNameInnerType() {
        Map<String, Integer> map = new HashMap<>();
        functionDescriptorMap.forEach((key, value) -> map.put(key, value.innerType()));
        return map;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {

        List<Curd> parameterList = function.getParameterList();



        String functionName = (String) function.getMethod().getLiteral();
        functionDescriptorMap.get(functionName).staticCheck(function, type);
    }

    @Override
    public void error(int errorCode) {
        errorReporterDelegate.error(errorCode);
    }

    @Override
    public void error(int errorCode, ROError attachment) {
        errorReporterDelegate.error(errorCode, attachment);
    }

}

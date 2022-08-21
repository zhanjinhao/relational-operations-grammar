package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.AbstractFunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptorROErrorReporterDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/7/24 23:21
 */
public abstract class AbstractFunctionEvaluator implements FunctionEvaluator<FunctionDescriptor>, ROErrorReporter {

    private final HashMap<String, FunctionDescriptor> functionDescriptorMap = new HashMap<>();

    private final ROErrorReporter errorReporterDelegate = new FunctionDescriptorROErrorReporterDelegate();

    @Override
    public void addFunction(Class<? extends FunctionDescriptor> function) {
        if (function == null) {
            return;
        }
        try {
            FunctionDescriptor functionDescriptor;
            // 对于 AbstractFunctionHandler 来说，需要调用带 FunctionEvaluator 参数的构造方法
            if (AbstractFunctionDescriptor.class.isAssignableFrom(function)) {
                Constructor<? extends FunctionDescriptor> constructor = function.getConstructor(FunctionEvaluator.class);
                functionDescriptor = constructor.newInstance(this);
            } else {
                functionDescriptor = function.newInstance();
            }
            if (functionDescriptorMap.containsKey(functionDescriptor.functionName())) {
                error(FunctionDescriptorROErrorReporterDelegate.FUNCTION_DESCRIPTOR_REPEATED_PARSE);
                return;
            }
            functionDescriptorMap.put(functionDescriptor.functionName(), functionDescriptor);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            error(FunctionDescriptorROErrorReporterDelegate.FUNCTION_HANDLER_INSTANTIATION_PARSE, e);
        }
    }

    @Override
    public FunctionDescriptor getFunction(String functionName) {
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

    @Override
    public void error(int errorCode, Throwable throwable) {
        errorReporterDelegate.error(errorCode, throwable);
    }
}

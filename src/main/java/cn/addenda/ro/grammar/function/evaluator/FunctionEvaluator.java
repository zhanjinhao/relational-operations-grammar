package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.ast.expression.CurdType;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.descriptor.FunctionDescriptor;

import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/4/11 14:02
 */
public interface FunctionEvaluator<T extends FunctionDescriptor> extends ROError {

    String name();

    FunctionDescriptor getFunction(String functionName);

    Set<String> functionNameSet();

    Map<String, Integer> functionNameInnerType();

    void staticCheck(Function function, CurdType type);

    void addFunction(Class<? extends T> function);

}

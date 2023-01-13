package cn.addenda.ro.grammar.function.descriptor;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.AbstractROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.FunctionException;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/7/27 22:23
 */
public class FunctionDescriptorROErrorReporterDelegate extends AbstractROErrorReporterDelegate {

    @Override
    public void fillErrorMsg() {
        addErrorMsg(FUNCTION_parameter_PARSE, FUNCTION_parameter_PARSE_MSG);
        addErrorMsg(FUNCTION_DESCRIPTOR_REPEATED_PARSE, FUNCTION_DESCRIPTOR_REPEATED_PARSE_MSG);
        addErrorMsg(FUNCTION_HANDLER_INSTANTIATION_PARSE, FUNCTION_HANDLER_INSTANTIATION_PARSE_MSG);

        addSuffixFunction(Function.class, (error) -> {
            Function function = (Function) error;
            Token method = function.getMethod();
            return "Current method is: " + method.getLiteral() + ", and current index is: " + method.getIndex() + ".";
        });

    }

    @Override
    public void error(int errorCode) {
        throw new FunctionException(errorCode, getErrorMsg(errorCode));
    }

    @Override
    public void error(int errorCode, ROError roError) {
        throw new FunctionException(errorCode, getErrorMsg(errorCode) + SEPARATOR + getSuffix(roError));
    }

    public static final int FUNCTION_parameter_PARSE = 50001;
    public static final String FUNCTION_parameter_PARSE_MSG = "Illegal function parameter. ";

    public static final int FUNCTION_DESCRIPTOR_REPEATED_PARSE = 51000;
    public static final String FUNCTION_DESCRIPTOR_REPEATED_PARSE_MSG = "FunctionDescriptor has existed: ";

    public static final int FUNCTION_HANDLER_INSTANTIATION_PARSE = 52000;
    public static final String FUNCTION_HANDLER_INSTANTIATION_PARSE_MSG = "Failed to instantiate functionDescriptor: ";

}

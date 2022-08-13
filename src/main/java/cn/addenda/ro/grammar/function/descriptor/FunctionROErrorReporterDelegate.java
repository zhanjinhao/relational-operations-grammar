package cn.addenda.ro.grammar.function.descriptor;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.AbstractROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.function.FunctionException;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * AstROErrorReporterDelegate 和 FunctionROErrorReporterDelegate 区别：
 * 一个SQL语句对应一个Ast，所以，将tokenSequence传入到AstROErrorReporterDelegate中，就可以将Ast当ROError用。
 * FunctionHandler会在多个Ast中被使用，所以不能将Function传入FunctionROErrorReporterDelegate，即无法将FunctionHandler当ROError用。
 *
 * @author addenda
 * @datetime 2021/7/27 22:23
 */
public class FunctionROErrorReporterDelegate extends AbstractROErrorReporterDelegate {

    @Override
    public void fillErrorMsg() {
        addErrorMsg(FUNCTION_parameter_PARSE, FUNCTION_parameter_PARSE_MSG);
        addErrorMsg(FUNCTION_unkonw_EVALUATE, FUNCTION_unkonw_EVALUATE_MSG);
        addErrorMsg(FUNCTION_dateType_EVALUATE, FUNCTION_dateType_EVALUATE_MSG);
        addErrorMsg(FUNCTION_dateParameter_EVALUATE, FUNCTION_dateParameter_EVALUATE_MSG);
        addErrorMsg(FUNCTION_formatPattern_EVALUATE, FUNCTION_formatPattern_EVALUATE_MSG);
        addErrorMsg(FUNCTION_repeatedPattern_EVALUATE, FUNCTION_repeatedPattern_EVALUATE_MSG);
        addErrorMsg(FUNCTION_HANDLER_REPEATED_PARSE, FUNCTION_HANDLER_REPEATED_PARSE_MSG);
        addErrorMsg(FUNCTION_HANDLER_INSTANTIATION_PARSE, FUNCTION_HANDLER_INSTANTIATION_PARSE_MSG);

        addSuffixFunction(Function.class, (error) -> {
            Function function = (Function) error;
            Token method = function.getMethod();
            return "Current method is: " + method.getLiteral() + ", and current index is: " + method.getIndex() + ".";
        });

        addSuffixFunction(FunctionDescriptor.class, (error) -> {
            FunctionDescriptor functionDescriptor = (FunctionDescriptor) error;
            return "Current functionHandler is: " + functionDescriptor.functionName() + ".";
        });

    }

    @Override
    public void error(int errorCode) {
        throw new FunctionException(errorCode, getErrorMsg(errorCode));
    }

    @Override
    public void error(int errorCode, ROError attachment) {
        throw new FunctionException(errorCode, getErrorMsg(errorCode) + SEPARATOR + getSuffix(attachment));
    }

    @Override
    public void error(int errorCode, Throwable throwable) {
        throw new FunctionException(errorCode, getErrorMsg(errorCode), throwable);
    }

    public static final int FUNCTION_parameter_PARSE = 50001;
    public static final String FUNCTION_parameter_PARSE_MSG = "Illegal function parameter. ";

    public static final int FUNCTION_unkonw_EVALUATE = 50002;
    public static final String FUNCTION_unkonw_EVALUATE_MSG = "Error occur when evaluating function. ";

    public static final int FUNCTION_dateType_EVALUATE = 50003;
    public static final String FUNCTION_dateType_EVALUATE_MSG = "Unsupported date type. ";

    public static final int FUNCTION_dateParameter_EVALUATE = 50004;
    public static final String FUNCTION_dateParameter_EVALUATE_MSG = "Unsupported date parameter. ";

    public static final int FUNCTION_formatPattern_EVALUATE = 50005;
    public static final String FUNCTION_formatPattern_EVALUATE_MSG = "Unsupported format pattern. ";

    public static final int FUNCTION_repeatedPattern_EVALUATE = 50006;
    public static final String FUNCTION_repeatedPattern_EVALUATE_MSG = "Repeated format pattern. ";

    public static final int FUNCTION_HANDLER_REPEATED_PARSE = 51000;
    public static final String FUNCTION_HANDLER_REPEATED_PARSE_MSG = "FunctionHandler has existed: ";

    public static final int FUNCTION_HANDLER_INSTANTIATION_PARSE = 52000;
    public static final String FUNCTION_HANDLER_INSTANTIATION_PARSE_MSG = "Failed to instantiate functionHandler: ";

}

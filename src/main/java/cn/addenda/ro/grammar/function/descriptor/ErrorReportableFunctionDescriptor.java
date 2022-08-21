package cn.addenda.ro.grammar.function.descriptor;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.ROErrorReporter;

/**
 * @author addenda
 * @datetime 2021/7/27 22:15
 */
public abstract class ErrorReportableFunctionDescriptor implements FunctionDescriptor, ROErrorReporter {

    private final ROErrorReporter errorReporter = new FunctionDescriptorROErrorReporterDelegate();

    @Override
    public void error(int errorCode) {
        errorReporter.error(errorCode);
    }

    @Override
    public void error(int errorCode, ROError attachment) {
        errorReporter.error(errorCode, attachment);
    }

}

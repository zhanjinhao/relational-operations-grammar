package cn.addenda.ro.grammar.function;

import cn.addenda.ro.grammar.GrammarException;

/**
 * @author addenda
 * @datetime 2021/7/24 23:07
 */
public class FunctionException extends GrammarException {
    public FunctionException(int errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

    public FunctionException(int errorCode, String errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }

}

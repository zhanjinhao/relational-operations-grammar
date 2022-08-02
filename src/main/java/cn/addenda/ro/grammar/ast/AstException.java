package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.GrammarException;

/**
 * @author addenda
 * @datetime 2021/2/24 11:07
 */
public class AstException extends GrammarException {

    public AstException(int errorCode, String message) {
        super(errorCode, message);
    }

    public AstException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}

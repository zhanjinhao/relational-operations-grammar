package cn.addenda.ro.grammar.lexical;

import cn.addenda.ro.grammar.GrammarException;

/**
 * @author addenda
 * @datetime 2021/2/24 10:05
 */
public class LexicalException extends GrammarException {

    public LexicalException(int errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

}

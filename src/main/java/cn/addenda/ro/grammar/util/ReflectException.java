package cn.addenda.ro.grammar.util;

/**
 * @author addenda
 * @datetime 2022/1/27 9:52
 */
public class ReflectException extends RuntimeException {

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

}

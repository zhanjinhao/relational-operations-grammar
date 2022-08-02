package cn.addenda.ro;

/**
 * @author addenda
 * @datetime 2021/7/2 15:29
 */
public class ROException extends RuntimeException {

    /**
     * 词法错误：1xxxx
     * 语法错误：2xxxx
     * 实体错误：3xxxx
     * 脚本错误：4xxxx
     * 函数错误：5xxxx
     * 运算错误：6xxxx
     * 数据类型错误：7xxxx
     * <p>
     * 启动错误：9xxxx
     */
    private final int errorCode;

    private final String errorMsg;

    public ROException(int errorCode, String errorMsg) {
        super("errorCode : " + errorCode + ". " + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ROException(int errorCode, String errorMsg, Throwable cause) {
        super("errorCode : " + errorCode + ". " + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}

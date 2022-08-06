package cn.addenda.ro.data;

/**
 * @author addenda
 * @datetime 2022/5/10 19:55
 */
public class DataTypeConst {

    private DataTypeConst() {
    }

    public static final int STRING = 1;
    public static final int INTEGER = 2;
    public static final int DATE = 4;
    public static final int DECIMAL = 8;
    public static final int BOOLEAN = 16;

    public static final int ALL = STRING | INTEGER | DATE | DECIMAL | BOOLEAN;

}

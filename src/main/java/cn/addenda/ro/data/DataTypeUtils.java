package cn.addenda.ro.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author addenda
 * @datetime 2022/5/10 19:56
 */
public class DataTypeUtils {

    private DataTypeUtils() {
    }

    public static int getInnerDataType(Object object) {
        if (object == null) {
            return DataTypeConst.ALL;
        }
        Class<?> aClass = object.getClass();
        return getInnerDataType(aClass);
    }

    public static int getInnerDataType(Class<?> aClass) {
        if (String.class.isAssignableFrom(aClass)) {
            return DataTypeConst.STRING;
        } else if (Long.class.isAssignableFrom(aClass) || long.class.isAssignableFrom(aClass)
                || Integer.class.isAssignableFrom(aClass) || int.class.isAssignableFrom(aClass)
                || BigInteger.class.isAssignableFrom(aClass) || BigDecimal.class.isAssignableFrom(aClass)
                || Short.class.isAssignableFrom(aClass) || short.class.isAssignableFrom(aClass)) {
            return DataTypeConst.NUMBER;
        } else if (Date.class.isAssignableFrom(aClass) || LocalDateTime.class.isAssignableFrom(aClass)
                || LocalDate.class.isAssignableFrom(aClass) || LocalTime.class.isAssignableFrom(aClass)) {
            return DataTypeConst.DATE;
        }
        throw new DataTypeException("不支持的数据类型！" + aClass.getName());
    }

    public static boolean isString(int dataType) {
        return (DataTypeConst.STRING & dataType) != 0;
    }

    public static boolean isNumber(int dataType) {
        return (DataTypeConst.NUMBER & dataType) >> 1 != 0;
    }

    public static boolean isDate(int dataType) {
        return (DataTypeConst.DATE & dataType) >> 2 != 0;
    }

    public static boolean conflict(int left, int right) {
        return (left & right) > 0;
    }

}

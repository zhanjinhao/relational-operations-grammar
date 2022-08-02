package cn.addenda.ro.data;

import cn.addenda.ro.ROException;

/**
 * @author addenda
 * @datetime 2022/5/10 19:58
 */
public class DataTypeException extends ROException {

    public DataTypeException(String errorMsg) {
        super(70000, errorMsg);
    }

}

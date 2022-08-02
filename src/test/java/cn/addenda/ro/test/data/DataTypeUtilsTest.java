package cn.addenda.ro.test.data;

import cn.addenda.ro.data.DataTypeUtils;

/**
 * @Author ISJINHAO
 * @Date 2022/5/10 20:16
 */
public class DataTypeUtilsTest {

    public static void main(String[] args) {

//        System.out.println(DataTypeUtils.isString(DataTypeConst.STRING));
//        System.out.println(DataTypeUtils.isString(DataTypeConst.NUMBER));
//        System.out.println(DataTypeUtils.isString(DataTypeConst.DATE));
//
//        System.out.println(DataTypeUtils.isNumber(DataTypeConst.STRING));
//        System.out.println(DataTypeUtils.isNumber(DataTypeConst.NUMBER));
//        System.out.println(DataTypeUtils.isNumber(DataTypeConst.DATE));
//
//        System.out.println(DataTypeUtils.isDate(DataTypeConst.STRING));
//        System.out.println(DataTypeUtils.isDate(DataTypeConst.NUMBER));
//        System.out.println(DataTypeUtils.isDate(DataTypeConst.DATE));


        System.out.println(DataTypeUtils.conflict(1, 1));
        System.out.println(DataTypeUtils.conflict(1, 2));
        System.out.println(DataTypeUtils.conflict(3, 2));
        System.out.println(DataTypeUtils.conflict(5, 2));

    }

}

package cn.addenda.ro.test.data;

import cn.addenda.ro.data.DataTypeUtils;

/**
 * @Author ISJINHAO
 * @Date 2022/5/10 20:16
 */
public class DataTypeUtilsTest {

    public static void main(String[] args) {

        System.out.println(DataTypeUtils.conflict(1, 1));
        System.out.println(DataTypeUtils.conflict(1, 2));
        System.out.println(DataTypeUtils.conflict(3, 2));
        System.out.println(DataTypeUtils.conflict(5, 2));

    }

}

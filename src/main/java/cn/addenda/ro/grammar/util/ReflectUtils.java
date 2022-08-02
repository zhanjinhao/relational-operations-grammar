package cn.addenda.ro.grammar.util;

import java.lang.reflect.Field;

/**
 * @author addenda
 * @datetime 2022/1/27 9:50
 */
public class ReflectUtils {

    public static void setFieldValue(Object object, String name, Object replacement) {
        try {
            // 获取obj类的字节文件对象
            Class<?> c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(name);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, replacement);
        } catch (Exception e) {
            throw new ReflectException("设置属性值时出错！", e);
        }
    }

}

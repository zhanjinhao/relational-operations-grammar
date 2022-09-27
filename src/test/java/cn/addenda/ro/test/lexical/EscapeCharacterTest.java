package cn.addenda.ro.test.lexical;

/**
 * @author addenda
 * @datetime 2022/9/11 16:25
 */
public class EscapeCharacterTest {

    public static void main(String[] args) {
        String a = "\'a\'";
        System.out.println(a.replace("\\", "\\\\"));

        String regex = "^\\\\$";

        System.out.println("\\".matches(regex));
        System.out.println("\\\\".matches(regex));

    }

}

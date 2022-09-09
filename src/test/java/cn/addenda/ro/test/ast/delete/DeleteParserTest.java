package cn.addenda.ro.test.ast.delete;

import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class DeleteParserTest {

    static String[] sqls = new String[]{
            "delete from A where id in (1, 2, 3)"
    };

    public static void main(String[] args) {
        test1();
        test1();
    }

    private static void test1() {
        for (String sql : SqlReader.read("src/test/resources/delete.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);

//            System.out.println(parse);

            String s1 = sql.replaceAll("\\s+", "");
            String s2 = parse.toString().replaceAll("\\s+", "");

            if (s1.equalsIgnoreCase(s2)) {
                System.out.println(s1);
                System.out.println(s2);
            } else {
                System.err.println(s1);
                System.err.println(s2);
            }
        }
    }

    private static void test2() {
        for (String sql : SqlReader.read("src/test/resources/delete.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);

            String s2 = parse.toString(" ");
            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }
    }

}

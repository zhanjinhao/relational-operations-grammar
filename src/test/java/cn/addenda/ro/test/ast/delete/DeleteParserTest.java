package cn.addenda.ro.test.ast.delete;

import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class DeleteParserTest {

    static String[] sqls = new String[]{
            "delete from score where CREATE_TM < date_add(now(), interval 1 day) and DEGREE + 1 < 60 - 1",
            "delete from score where DEGREE < 50",
            "delete from score where CREATE_TM < now()",
            "delete from score where DEGREE + 1 < 60 - 1",
            "delete from score"
    };

    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        for (String sql : sqls) {
            DeleteParser deleteParser = CurdParserFactory.createDeleteParser(sql);
            Curd parse = deleteParser.parse();

//            System.out.println(parse);

            String s1 = sql.replaceAll("\\s+", "");
            String s2 = parse.toString().replaceAll("\\s+", "");

            if (s1.equals(s2)) {
                System.out.println(s1);
                System.out.println(s2);
            } else {
                System.err.println(s1);
                System.err.println(s2);
            }
        }
    }

    private static void test2() {
        for (String sql : sqls) {
            DeleteParser deleteParser = CurdParserFactory.createDeleteParser(sql);
            Curd parse = deleteParser.parse();

            String s2 = parse.toString(" ");
            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }
    }

}

package cn.addenda.ro.test.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2021/12/23 15:22
 */
public class SelectParserTest {

    static String[] sqls = new String[]{

    };

    public static void main(String[] args) {
        test2();
    }

    private static void test1() {

        for (String sql : SqlReader.read("src/test/resources/select.test", sqls)) {
            SelectParser selectParser = CurdParserFactory.createSelectParser(sql);
            Curd parse = selectParser.parse();

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

        for (String sql : SqlReader.read("src/test/resources/select.test", sqls)) {
            SelectParser selectParser = CurdParserFactory.createSelectParser(sql);
            Curd parse = selectParser.parse();

//            System.out.println(parse);

            String s2 = parse.toString(" ");

            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }

    }


}

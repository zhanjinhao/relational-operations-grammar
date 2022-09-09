package cn.addenda.ro.test.ast.update;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class UpdateParserTest {


    static String[] sqls = new String[]{
        "update A set a = 1 where id in (1, 2, 3)"
    };

    public static void main(String[] args) {
        test1();
        test1();
    }

    private static void test1() {
        for (String sql : SqlReader.read("src/test/resources/update.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);

            String s1 = sql.replaceAll("\\s+", "").toLowerCase();
            String s2 = parse.toString().replaceAll("\\s+", "").toLowerCase();

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
        for (String sql : SqlReader.read("src/test/resources/update.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);
            AstMetaData astMetaData = parse.getAstMetaData();
            System.out.println(astMetaData.getParameterCount());
            System.out.println(astMetaData.getHashMarkCount());

            String s2 = parse.toString(" ");
            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }
    }

}

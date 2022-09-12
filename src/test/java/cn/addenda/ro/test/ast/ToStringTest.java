package cn.addenda.ro.test.ast;

import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @author addenda
 * @datetime 2022/9/12 16:09
 */
public class ToStringTest {


    static String[] sqls = new String[]{

    };

    public static void main(String[] args) {

        for (String sql : SqlReader.read(sqls,
                "src/test/resources/insert.test",
                "src/test/resources/delete.test",
                "src/test/resources/select.test",
                "src/test/resources/update.test")) {
            Curd curd = CurdUtils.parse(sql);

            String s1 = curd.toString("").replace("\t", "").replace("\n", "");

            if (s1.contains(" ")) {
                System.err.println(s1);
            } else {
                System.out.println(s1);
            }

        }
    }

}

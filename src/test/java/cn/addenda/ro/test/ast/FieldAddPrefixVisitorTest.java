package cn.addenda.ro.test.ast;

import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2022/2/1 14:24
 */
public class FieldAddPrefixVisitorTest {

    static String[] sqls = new String[]{

    };

    public static void main(String[] args) {

        for (String sql : SqlReader.read(sqls,
                "src/test/resources/insert.test",
                "src/test/resources/delete.test",
                "src/test/resources/select.test",
                "src/test/resources/update.test")) {
            Curd curd = CurdUtils.parse(sql);

            Curd deepClone = curd.deepClone();

            String tableName = "HAHAHAHAHAHA";

            deepClone.fieldAddPrefix(tableName);
            String s1 = curd.toString().replaceAll("\\s+", "");
            String after = deepClone.toString().replaceAll("\\s+", "");
            String s2 = after.replaceAll(tableName + ".", "");

            if (s1.equals(s2)) {
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(after);
            } else {
                System.err.println(s1);
                System.err.println(s2);
                System.out.println(after);
            }

        }

    }

}

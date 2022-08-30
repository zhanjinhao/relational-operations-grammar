package cn.addenda.ro.test.ast.update;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.update.UpdateParser;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class UpdateParserTest {


    static String[] sqls = new String[]{
        "update runoob_tbl set runoob_title=replace( runoob_title , 'c++', 'python' ) , a=?  + 1 , b=?"
    };

    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        for (String sql : SqlReader.read("src/test/resources/update.test", sqls)) {
            UpdateParser updateParser = CurdParserFactory.createUpdateParser(sql);
            Curd parse = updateParser.parse();

            System.out.println(parse);

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
        for (String sql : SqlReader.read("src/test/resources/update.test", sqls)) {
            UpdateParser updateParser = CurdParserFactory.createUpdateParser(sql);
            Curd parse = updateParser.parse();
            AstMetaData astMetaData = parse.getAstMetaData();
            System.out.println(astMetaData.getParameterCount());
            System.out.println(astMetaData.getHashMarkCount());

            String s2 = parse.toString(" ");
            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }
    }

}

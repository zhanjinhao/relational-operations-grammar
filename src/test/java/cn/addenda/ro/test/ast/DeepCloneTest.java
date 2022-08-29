package cn.addenda.ro.test.ast;

import cn.addenda.ro.grammar.ast.CurdParser;
import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.function.evaluator.DefaultFunctionEvaluator;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2022/1/27 15:40
 */
public class DeepCloneTest {


    static String[] sqls = new String[]{

    };

    public static void main(String[] args) {

        for (String sql : SqlReader.read(sqls,
                "src/test/resources/insert.test",
                "src/test/resources/delete.test",
                "src/test/resources/select.test",
                "src/test/resources/update.test")) {
            CurdParser curdParser = CurdParserFactory.createCurdParser(sql, DefaultFunctionEvaluator.getInstance());

            Curd curd = curdParser.parse();
            Curd deepClone = curd.deepClone();

            String s1 = curd.toString().replaceAll("\\s+", "");
            String s2 = deepClone.toString().replaceAll("\\s+", "");

            if (s1.equals(s2)) {
                System.out.println(s1);
                System.out.println(s2);
            } else {
                System.err.println(s1);
                System.err.println(s2);
            }

        }
    }

}

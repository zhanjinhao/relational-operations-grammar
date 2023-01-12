package cn.addenda.ro.test.ast.retrieve;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @Author ISJINHAO
 * @Date 2021/12/23 15:22
 */
public class SelectParserTest {

    static String[] sqls = new String[]{

            "select timestampdiff(day, '2010-01-01', '2010-06-01') as result from dual",
            "select a from A where (select c from C) and (select d from D)",
            "select a, b, (select c from C) as c from AB",
            "insert  into table_listnames ( name, address, tele )   select *  from  (  select 'rupert', 'somewhere', '022' from dual   )  tmp  where  not exists  (  select name  from table_listnames   where name  = 'rupert'  )  limit 1"
//            "select\n" +
//                    "    productline,\n" +
//                    "    ordervalue,\n" +
//                    "    round(\n" +
//                    "       percent_rank() over (\n" +
//                    "          order by ordervalue\n" +
//                    "       )\n" +
//                    "    ,2) as percentile_rank\n" +
//                    "from\n" +
//                    "    t",
    };

    public static void main(String[] args) {
        test1();
        test1();
    }

    private static void test1() {

        for (String sql : SqlReader.read("src/test/resources/select.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);
            final AstMetaData astMetaData = parse.getAstMetaData();
//            System.out.println(astMetaData.getHashMarkCount());
//            System.out.println(astMetaData.getParameterCount());

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

        for (String sql : SqlReader.read("src/test/resources/select.test", sqls)) {
            Curd parse = CurdUtils.parse(sql);

//            System.out.println(parse);

            String s2 = parse.toString(" ");

            System.out.println(s2.replaceAll("\\t", "").replaceAll("\\n", ""));
        }

    }


}

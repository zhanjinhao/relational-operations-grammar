package cn.addenda.ro.test.ast.retrieve;

import cn.addenda.ro.grammar.ast.AstMetaData;
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
        " select T.FLIGHT_ID , ROUTE_TOWARDS  "
            + "from "
                + "T_DISPATCH_FLIGHT_RELEASE  RELEASE ,  "
                + "(  select FLIGHT_ID , max( MODIFY_TM  ) as LATEAST_TIME from T_DISPATCH_FLIGHT_RELEASE   where DELETE_FLAG  = 'N'  and FLIGHT_ID in ( ?, ?)  group by FLIGHT_ID )  T  "
            + "where RELEASE.FLIGHT_ID  = ?   and RELEASE.MODIFY_TM  = ?  and  T.FLIGHT_ID in ( ?, ?)  "
            + "order by RELEASE.CREATE_TIME limit ?",
        " select  case a  when b  + 1  then '1' when b  + 2  then '2' else '3' end as A from  (  select 2 as a, 1 as b from dual where a = ?  )  A where a = ?",
    };

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {

        for (String sql : SqlReader.read("src/test/resources/select.test", sqls)) {
            SelectParser selectParser = CurdParserFactory.createSelectParser(sql);
            Curd parse = selectParser.parse();
            final AstMetaData astMetaData = parse.getAstMetaData();
            System.out.println(astMetaData.getHashMarkCount());
            System.out.println(astMetaData.getParameterCount());

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

package cn.addenda.ro.test.ast.create;

import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.expression.Curd;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class InsertParserTest {

    static String[] sqls = new String[]{

//            "insert into score(SNO, CNO, DEGREE) values (109, '3-105', 76)",

            "insert into score(SNO, CNO, DEGREE) values (109, '3-105', DEGREE + 76) on duplicate key update SNO = 131, CNO = '4-111', DEGREE = DEGREE_MAX+1",
//
//            "insert into score(SNO, CNO, DEGREE) values (109, '3-105', 76), (109, '3-105', 76), (109, '3-105', 76)",
//
//            "insert into score(SNO, CNO, DEGREE) values (?, '3-105', ?), (109, ?, 76), (?, '3-105', ?)",
//
//            "insert into score set SNO = 109, CNO = '3-105', DEGREE = 76",

            "insert into score set SNO = 109, CNO = date_format(now(), 'yyyy-dd-mm'), DEGREE = DEGREE + 9 * 3",

            "insert ignore into score set SNO = 109, CNO = '3-105', DEGREE = 76",

            "insert ignore into score set SNO = ?, CNO = '3-105', DEGREE = ?",

            "insert ignore into score set SNO = '1387398', CNO = #{cno}, DEGREE = ?",

            "insert into table_listnames (name, address, tele) " +
                    "select * from (select 'rupert', 'somewhere', '022' from dual) tmp " +
                    "where not exists ( " +
                    "    select name from table_listnames where name = 'rupert' " +
                    ") limit 1"

    };

    public static void main(String[] args) {
        for (String sql : sqls) {
            InsertParser insertParser = CurdParserFactory.createInsertParser(sql);
            Curd parse = insertParser.parse();

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

}

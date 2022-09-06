package cn.addenda.ro.test.ast;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.test.SqlReader;

/**
 * @author addenda
 * @datetime 2022/8/16 13:42
 */
public class ResetAstMetaDataTest {


    static String[] sqls = new String[]{
        "insert ignore into score set SNO='1387398', CNO=#{cno}, DEGREE=?"
    };

    public static void main(String[] args) {

        for (String sql : SqlReader.read(sqls,
            "src/test/resources/insert.test",
            "src/test/resources/delete.test",
            "src/test/resources/select.test",
            "src/test/resources/update.test"
        )) {

            Curd curd = CurdUtils.parse(sql);

            final AstMetaData astMetaData1 = curd.getAstMetaData();
            String astStr1 = astMetaData1.toString();

            curd.reSetAstMetaData();
            final AstMetaData astMetaData2 = curd.getAstMetaData();
            String astStr2 = astMetaData2.toString();

            if (astStr1.equals(astStr2)) {
                System.out.println(astStr1);
                System.out.println(astStr2);
            } else {
                System.err.println(astStr1);
                System.err.println(astStr2);
            }

        }
    }

}

package cn.addenda.ro.test.lexical;

import cn.addenda.ro.grammar.lexical.scan.DefaultScanner;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.test.SqlReader;

/**
 * @author addenda
 * @datetime 2023/4/15 17:17
 */
public class EqualTest {

    static String[] sqls = new String[]{

    };

    public static void main(String[] args) {
        for (String sql : SqlReader.read(sqls,
                "src/test/resources/select.test",
                "src/test/resources/insert.test",
                "src/test/resources/delete.test",
                "src/test/resources/update.test")) {
            DefaultScanner defaultScanner = new DefaultScanner(sql);
            TokenSequence tokenSequence = defaultScanner.scanTokens();
            TokenSequence tokenSequence1 = tokenSequence.deepClone();
            System.out.println(tokenSequence1.equals(tokenSequence));
        }

        String sql1 = "insert a set `a1` = '123'";
        String sql2 = "insert a set a1 = '123'";

        TokenSequence tokenSequence1 = new DefaultScanner(sql1).scanTokens();
        TokenSequence tokenSequence2 = new DefaultScanner(sql2).scanTokens();
        System.out.println(tokenSequence2.equals(tokenSequence1));
    }

}

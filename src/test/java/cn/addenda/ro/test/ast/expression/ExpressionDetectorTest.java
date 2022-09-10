package cn.addenda.ro.test.ast.expression;

import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;

/**
 * @Author ISJINHAO
 * @Date 2022/1/5 19:30
 */
public class ExpressionDetectorTest {

    static String[] sqls = new String[]{
            "a > c + b and now() > e and t1.f is not null",
            "-(100+200)",
            "SCORE.DEGREE >= GRADE.LOW and SCORE.DEGREE <= GRADE.UPP"
    };

    public static void main(String[] args) {

        for (String sql : sqls) {
            Curd parse = CurdUtils.parse(sql);
            parse.accept(new ExpressionAstMetaDataDetector(null));
            System.out.println(parse.getAstMetaData());

        }

    }


}

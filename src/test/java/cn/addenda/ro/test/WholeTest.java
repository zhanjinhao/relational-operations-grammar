package cn.addenda.ro.test;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.test.ast.DeepCloneTest;
import cn.addenda.ro.test.ast.IdentifierFillTNVisitorTest;
import cn.addenda.ro.test.ast.ResetAstMetaDataTest;
import cn.addenda.ro.test.ast.create.InsertParserTest;
import cn.addenda.ro.test.ast.delete.DeleteParserTest;
import cn.addenda.ro.test.ast.retrieve.SelectParserTest;
import cn.addenda.ro.test.ast.update.UpdateParserTest;

import java.util.Set;

/**
 * @author addenda
 * @datetime 2022/9/6 17:08
 */
public class WholeTest {

    public static void main(String[] args) {
        SelectParserTest.main(args);
        DeleteParserTest.main(args);
        UpdateParserTest.main(args);
        InsertParserTest.main(args);
        DeepCloneTest.main(args);
        IdentifierFillTNVisitorTest.main(args);
        ResetAstMetaDataTest.main(args);

        Set<Class<?>> literalClassSet = Token.LITERAL_CLASS_SET;
        for (Class<?> c : literalClassSet) {
            System.out.println(c);
        }

    }

}

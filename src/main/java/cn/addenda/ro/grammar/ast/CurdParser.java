package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.Function;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.grammar.ast.update.UpdateParser;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.TokenType;

/**
 * @author addenda
 * @datetime 2021/4/10 9:52
 */
public class CurdParser extends AbstractCurdParser {

    // 这里可以对 Parser做一层缓存，避免每次都new对象引起Full GC。

    public CurdParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        super(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    @Override
    public Curd parse() {
        if (tokenSequence.curEqual(TokenType.SELECT)) {
            return new SelectParser(tokenSequence, getFunctionEvaluator(), detectAstMetaData).parse();
        } else if (tokenSequence.curEqual(TokenType.INSERT)) {
            return new InsertParser(tokenSequence, getFunctionEvaluator(), detectAstMetaData).parse();
        } else if (tokenSequence.curEqual(TokenType.DELETE)) {
            return new DeleteParser(tokenSequence, getFunctionEvaluator(), detectAstMetaData).parse();
        } else if (tokenSequence.curEqual(TokenType.UPDATE)) {
            return new UpdateParser(tokenSequence, getFunctionEvaluator(), detectAstMetaData).parse();
        }
        error(AstROErrorReporterDelegate.CURD_unknow_syntax_PARSE);
        return null;
    }

    @Override
    protected void doFunctionStaticCheck(Function function) {
        // nop
    }

}

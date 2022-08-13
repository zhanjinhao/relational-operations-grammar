package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteGrammarValidator;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

/**
 * @author addenda
 * @datetime 2021/4/8 20:37
 */
public class DeleteParser extends ExpressionParser {

    public DeleteParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    /**
     * delete              ->  "delete" "from" IDENTIFIER whereSeg
     * whereSeg ↑          ->  "where" logic
     * logic ↑             ->  condition (("or" | "and") condition)*
     * condition ↑         ->  comparison
     * comparison          ->  binaryArithmetic ((">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot) binaryArithmetic)?
     * binaryArithmetic ↑  ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     * unaryArithmetic ↑   ->  ("!"|"-") unaryArithmetic | primary
     * primary ↑           ->  #{xxx} | ? | "true" | "false" | "null" | INTEGER | STRING | IDENTIFIER | grouping | function
     * function ↑          ->  functionName "(" binaryArithmetic? ("," binaryArithmetic)* ")"
     */
    @Override
    public Curd parse() {
        Curd delete = delete();
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        delete.accept(new DeleteGrammarValidator(this.errorReporterDelegate));
        delete.accept(new DeleteAstMetaDataDetector(this.errorReporterDelegate));
        return delete;
    }

    private Curd delete() {
        consume(TokenType.DELETE, AstROErrorReporterDelegate.DELETE_delete_PARSE);
        consume(TokenType.FROM, AstROErrorReporterDelegate.DELETE_delete_PARSE);

        Token tableName = tokenSequence.takeCur();
        if (!TokenType.IDENTIFIER.equals(tableName.getType())) {
            error(AstROErrorReporterDelegate.DELETE_delete_PARSE);
        }
        tokenSequence.advance();

        Token token = tokenSequence.takeCur();
        if (TokenType.WHERE.equals(token.getType())) {
            Curd whereSeg = whereSeg();
            return new Delete(tableName, whereSeg);
        }
        return new Delete(tableName, null);
    }

}

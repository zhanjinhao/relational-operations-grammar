package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteGrammarValidator;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

/**
 * delete              ->  "delete" "from" IDENTIFIER (whereSeg)?<p/>
 * logic  ↑            ->  condition (("or" | "and") condition)*<p/>
 * condition  ↑        ->  inCondition | comparison<p/>
 * inCondition  ↑      ->  IDENTIFIER ("not")? "in" "(" (primary ("," primary)*) ")"<p/>
 * comparison  ↑       ->  binaryArithmetic (comparisonSymbol binaryArithmetic)?<p/>
 * comparisonSymbol ↑  ->  ">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot<p/>
 * isNot ↑             ->  "is" ("not")?<p/>
 * binaryArithmetic ↑  ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*<p/>
 * unaryArithmetic ↑   ->  ("!"|"-") unaryArithmetic | primary<p/>
 * primary ↑           ->  #{xxx} | ? | "true" | "false" | "null" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function<p/>
 * grouping ↑          ->  "(" logic ")"<p/>
 * <p/>
 * function ↑          ->  functionName "(" functionParameter? ("," functionParameter)* ")"<p/>
 * functionParameter ↑ ->  condition | timeInterval | timeUnit | function<p/>
 * timeInterval ↑      ->  "interval" INTEGER IDENTIFIER<p/>
 * timeUnit ↑          ->  IDENTIFIER "from" primary<p/>
 * <p/>
 * whereSeg ↑          ->  "where" logic<p/>
 *
 * @author addenda
 * @datetime 2021/4/8 20:37
 */
public class DeleteParser extends ExpressionParser {

    public DeleteParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        super(tokenSequence, functionEvaluator, detectAstMetaData);
    }


    @Override
    public Curd parse() {
        Curd delete = delete();
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        delete.accept(new DeleteGrammarValidator(this.errorReporterDelegate));
        if (detectAstMetaData) {
            delete.detectAstMetaData();
        }
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

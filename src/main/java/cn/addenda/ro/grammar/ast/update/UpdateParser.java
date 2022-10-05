package cn.addenda.ro.grammar.ast.update;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.AssignmentList;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.ast.update.visitor.UpdateGrammarValidator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

/**
 * update              ->  "update" IDENTIFIER "set" assignmentList (whereSeg)?<p/>
 * assignmentList ↑    ->  (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*<p/>
 * logic  ↑            ->  condition (("or" | "and") condition)*<p/>
 * condition  ↑        ->  inCondition | comparison<p/>
 * inCondition  ↑      ->  IDENTIFIER ("not")? "in" "(" (primary ("," primary)*) ")"<p/>
 * comparison  ↑       ->  binaryArithmetic (comparisonSymbol binaryArithmetic)?<p/>
 * comparisonSymbol ↑	->  ">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot<p/>
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
 * @datetime 2021/4/5 12:03
 */
public class UpdateParser extends ExpressionParser {

    public UpdateParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        super(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    @Override
    public Curd parse() {
        Curd update = update();
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        update.accept(new UpdateGrammarValidator(this.errorReporterDelegate));
        if (detectAstMetaData) {
            update.detectAstMetaData();
        }
        return update;
    }

    /**
     * "update" IDENTIFIER "set" assignmentList (whereSeg)?
     */
    private Curd update() {
        consume(TokenType.UPDATE, AstROErrorReporterDelegate.UPDATE_update_PARSE);

        Token tableName = tokenSequence.takeCur();
        if (!TokenType.IDENTIFIER.equals(tableName.getType())) {
            error(AstROErrorReporterDelegate.UPDATE_update_PARSE);
        }
        tokenSequence.advance();

        consume(TokenType.SET, AstROErrorReporterDelegate.UPDATE_update_PARSE);

        Curd curd = assignmentList();
        if (!(curd instanceof AssignmentList)) {
            error(AstROErrorReporterDelegate.UPDATE_update_PARSE);
        } else {
            AssignmentList assignmentList = (AssignmentList) curd;
            if (assignmentList.getEntryList() == null || assignmentList.getEntryList().isEmpty()) {
                error(AstROErrorReporterDelegate.UPDATE_update_PARSE);
            }
        }

        Token token = tokenSequence.takeCur();
        if (TokenType.WHERE.equals(token.getType())) {
            Curd whereSeg = whereSeg();
            return new Update(tableName, curd, whereSeg);
        }
        return new Update(tableName, curd, null);
    }

}

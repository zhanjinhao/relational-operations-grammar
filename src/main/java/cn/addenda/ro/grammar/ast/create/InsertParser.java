package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.create.visitor.InsertGrammarValidator;
import cn.addenda.ro.grammar.ast.expression.AssignmentList;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.ExpressionParser;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.grammar.ast.retrieve.SelectType;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelectType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * insert              ->  "insert" (constrict)? "into" IDENTIFIER (((insertValuesRep | insertSetRep) onDuplicateKey?) | (insertSelectRep))<p/>
 * constrict           ->  ignore<p/>
 * insertValuesRep     ->  "(" columnList ")" "values" ("(" binaryArithmetic ("," binaryArithmetic)* ")") ("," "(" binaryArithmetic ("," binaryArithmetic)* ")")*<p/>
 * insertSetRep        ->  "set" assignmentList<p/>
 * insertSelectRep     ->  "(" columnList ")" select<p/>
 * binaryArithmetic ↑  ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*<p/>
 * unaryArithmetic ↑   ->  ("!"|"-") unaryArithmetic | primary<p/>
 * primary ↑           ->  #{xxx} | ? | "true" | "false" | "null" | INTEGER | STRING | IDENTIFIER | grouping | function<p/>
 * function ↑          ->  functionName "(" binaryArithmetic? ("," binaryArithmetic)* ")"<p/>
 * onDuplicateKey      ->  "on" "duplicate" "key" "update" assignmentList<p/>
 * assignmentList ↑    ->  (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*<p/>
 * columnList ↑	       ->  IDENTIFIER ("," IDENTIFIER)*<p/>
 *
 * @author addenda
 * @datetime 2021/4/3 17:50
 */
public class InsertParser extends ExpressionParser {

    public InsertParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
        super(tokenSequence, functionEvaluator, detectAstMetaData);
    }

    @Override
    public Curd parse() {
        Curd insert = insert();
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        insert.accept(new InsertGrammarValidator(this.errorReporterDelegate));
        if (detectAstMetaData) {
            insert.detectAstMetaData();
        }
        return insert;
    }

    /**
     * "insert" (constrict)? "into" IDENTIFIER (((insertValuesRep | insertSetRep) onDuplicateKey?) | (insertSelectRep))
     */
    private Curd insert() {
        consume(TokenType.INSERT, AstROErrorReporterDelegate.INSERT_insert_PARSE);

        Token token = tokenSequence.takeCur();
        Token constrict = null;
        if (TokenType.IGNORE.equals(token.getType())) {
            constrict = token;
            tokenSequence.advance();
        }
        consume(TokenType.INTO, AstROErrorReporterDelegate.INSERT_insert_PARSE);

        Token tableName = tokenSequence.takeCur();
        tokenSequence.advance();

        InsertType insertType = null;
        Curd curd = null;
        if (tokenSequence.curEqual(TokenType.SET)) {
            curd = insertSetRep();
            insertType = InsertType.SET;
        } else {
            // valuesRep or selectRep
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.INSERT_insert_PARSE);
            List<Token> tokens = columnList();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.INSERT_insert_PARSE);
            if (tokenSequence.curEqual(TokenType.SELECT)) {
                curd = insertSelectRep(tokens);
                insertType = InsertType.SELECT;
            } else if (tokenSequence.curEqual(TokenType.VALUES)) {
                curd = insertValuesRep(tokens);
                insertType = InsertType.VALUES;
            } else {
                error(AstROErrorReporterDelegate.INSERT_insert_PARSE);
            }
        }

        if (!tokenSequence.checkExistsToken()) {
            return new Insert(constrict, tableName, curd, insertType);
        }

        return new Insert(constrict, tableName, curd, onDuplicateKey(), insertType);
    }

    /**
     * "(" columnList ")" select
     */
    private Curd insertSelectRep(List<Token> tokens) {
        InsertSelectParser insertSelectParser = new InsertSelectParser(this.tokenSequence, getFunctionEvaluator(), detectAstMetaData);
        Select select = (Select) insertSelectParser.parse();
        return new InsertSelectRep(tokens, select);
    }

    /**
     * "on" "duplicate" "key" "update" assignmentList
     */
    private Curd onDuplicateKey() {
        consume(TokenType.ON, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.DUPLICATE, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.KEY, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.UPDATE, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);

        Curd curd = assignmentList();
        return new OnDuplicateKey(curd);
    }


    /**
     * "(" columnList ")" "values" ("(" binaryArithmetic ("," binaryArithmetic)* ")") ("," "(" binaryArithmetic ("," binaryArithmetic)* ")")*
     */
    private Curd insertValuesRep(List<Token> tokenList) {

        consume(TokenType.VALUES, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);

        int currentCount = -1;
        if (tokenList != null) {
            currentCount = tokenList.size();
        }
        List<List<Curd>> curdListList = new ArrayList<>();
        // 处理多行的值
        do {
            List<Curd> curdList = new ArrayList<>();
            // 处理一行的值
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            do {
                Curd binaryArithmetic = binaryArithmetic();
                curdList.add(binaryArithmetic);
            } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            if (curdList.size() == currentCount || currentCount == -1) {
                currentCount = curdList.size();
            } else {
                error(AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            }
            curdListList.add(curdList);
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return new InsertValuesRep(tokenList, curdListList);
    }

    /**
     * "set" assignmentList
     */
    private Curd insertSetRep() {
        consume(TokenType.SET, AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
        Curd curd = assignmentList();
        if (!(curd instanceof AssignmentList)) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
            return null;
        }
        AssignmentList assignmentList = (AssignmentList) curd;
        if (assignmentList.getEntryList() == null || assignmentList.getEntryList().isEmpty()) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
        }
        return new InsertSetRep(curd);
    }


    private static class InsertSelectParser extends SelectParser {

        public InsertSelectParser(TokenSequence tokenSequence, FunctionEvaluator<?> functionEvaluator, boolean detectAstMetaData) {
            super(tokenSequence, functionEvaluator, detectAstMetaData);
        }

        @Override
        public Curd parse() {
            Select select = (Select) select();
            saveSelectType(select, SingleSelectType.INSERT, SelectType.VIEW);
            return select;
        }

    }

}

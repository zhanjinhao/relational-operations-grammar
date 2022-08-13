package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectGrammarValidator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/3/2
 */
public class SelectParser extends ExpressionParser {

    private static final SelectAstMetaDataDetector SELECT_AST_META_DATA_DETECTOR = new SelectAstMetaDataDetector();

    public SelectParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    /**
     * select              ->  singleSelect (("union" | "minus" | "intersect" | "except") ("all")? singleSelect)*
     * <p>
     * singleSelect        ->  columnSeg tableSeg (whereSeg)? (groupBySeg)? (orderBySeg)? (limitSeg)?
     * columnSeg           ->  "select" ("distinct")? columnRep ("," columnRep)*
     * columnRep           ->  (* | binaryArithmetic | caseWhen) ("as" IDENTIFIER)?
     * caseWhen            ->  "case" binaryArithmetic ("when" binaryArithmetic "then" binaryArithmetic)+ "else" binaryArithmetic "end"
     * tableSeg            ->  "from" tableRep (("left" | "right" | "cross")? ("join" | ",") tableRep ("on" logic)?)*
     * tableRep            ->  ("(" select ")" | IDENTIFIER) ("as" IDENTIFIER)?
     * whereSeg ↑          ->  "where" logic
     * groupBySeg          ->  "group" "by" IDENTIFIER ("," IDENTIFIER)* ("having" logic)?
     * orderBySeg          ->  "order" "by" IDENTIFIER ("desc" | "asc") ("," IDENTIFIER ("desc" | "asc"))*
     * limitSeg            ->  "limit" INTEGER ("," INTEGER)?
     * <p>
     * logic ↑             ->  condition (("or" | "and") condition)*
     * condition ↑+        ->  inCondition | existsCondition | comparison
     * inCondition         ->  IDENTIFIER ("not")? "in" "(" select | (primary (, primary)*) ")"
     * existsCondition     ->  ("not")? "exists" "(" select ")"
     * comparison ↑        ->  binaryArithmetic ((">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot) binaryArithmetic)?
     * isNot ↑             ->  "is" ("not")?
     * binaryArithmetic ↑  ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     * unaryArithmetic ↑   ->  ("!"|"-") unaryArithmetic | primary
     * primary ↑+          ->  #{xxx} | ? | "true" | "false" | "null" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function | "(" singleSelect ")" | groupFunction
     * grouping ↑          ->  "(" logic ")"
     * function ↑          ->  functionName "(" functionParameter? ("," functionParameter)* ")"
     * functionParameter ↑ ->  logic | timeInterval | timeUnit | function
     * timeInterval ↑      ->  "interval" INTEGER IDENTIFIER
     * timeUnit ↑          ->  IDENTIFIER "from" primary
     * groupFunction       ->  ("avg" | "max" | "min" | "count" | "sum" | "flat") "(" binaryArithmetic ")"
     * columnList ↑	       ->  IDENTIFIER ("," IDENTIFIER)*
     */
    @Override
    public Curd parse() {
        Select select = (Select) select();
        select.setSelectType(SelectType.VIEW);
        saveSingleSelectContext(select, SingleSelectType.TOP);
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        select.accept(new SelectGrammarValidator(this.errorReporterDelegate));
        select.accept(SELECT_AST_META_DATA_DETECTOR);
        return select;
    }


    /**
     * singleSelect (("union" | "minus" | "intersect" | "except") ("all")? singleSelect)*
     */
    protected Curd select() {
        int flag = 0;
        Curd left = singleSelect();
        while (tokenSequence.curEqual(TokenType.CUNION, TokenType.CMINUS, TokenType.CEXCEPT, TokenType.CINTERSECT)) {
            Token operator = tokenSequence.takeCur();
            tokenSequence.advance();
            Token allToken = null;
            if (tokenSequence.curEqual(TokenType.ALL)) {
                allToken = tokenSequence.takeCur();
                tokenSequence.advance();
            }
            Curd right = singleSelect();
            left = new Select(left, operator, allToken, right);
            flag++;
        }
        if (flag == 0) {
            left = new Select(left);
        }
        return left;
    }


    /**
     * columnSeg tableSeg (whereSeg)? (groupBySeg)? (orderBySeg)? (limitSeg)?
     */
    private Curd singleSelect() {
        Curd columnSeg = columnSeg();
        Curd tableSeg = tableSeg();
        // tableSeg不存在时在MySQL中是可以运行的，但是MySQL和Oracle都支持from dual的写法，
        // 为了统一，这里不支持不存在tableSeg的场景，只支持from dual的场景
        Curd whereSeg = null;
        if (tokenSequence.curEqual(TokenType.WHERE)) {
            whereSeg = whereSeg();
        }
        Curd groupBySeg = null;
        if (tokenSequence.curEqual(TokenType.GROUP)) {
            groupBySeg = groupBySeg();
        }
        Curd orderBySeg = null;
        if (tokenSequence.curEqual(TokenType.ORDER)) {
            orderBySeg = orderBySeg();
        }
        Curd limitSeg = null;
        if (tokenSequence.curEqual(TokenType.LIMIT)) {
            limitSeg = limitSeg();
        }
        return new SingleSelect(columnSeg, tableSeg, whereSeg, groupBySeg, orderBySeg, limitSeg);
    }


    /**
     * "select" ("distinct")? columnRep ("," columnRep)*
     */
    private Curd columnSeg() {
        if (!tokenSequence.curEqual(TokenType.SELECT)) {
            error(AstROErrorReporterDelegate.SELECT_columnSeg_PARSE);
        }
        tokenSequence.advance();
        Token restriction = null;
        if (tokenSequence.curEqual(TokenType.DISTINCT)) {
            restriction = tokenSequence.takeCur();
            tokenSequence.advance();
        }
        List<Curd> columnRepList = new ArrayList<>();
        do {
            columnRepList.add(columnRep());
        } while ((tokenSequence.equalThenAdvance(TokenType.COMMA)));
        return new ColumnSeg(restriction, columnRepList);
    }

    /**
     * (* | caseWhen | binaryArithmetic) ("as" IDENTIFIER)?
     */
    private Curd columnRep() {
        Curd column;
        if (tokenSequence.curEqual(TokenType.STAR)) {
            column = new Identifier(tokenSequence.takeCur());
            tokenSequence.advance();
        } else if (tokenSequence.curEqual(TokenType.CASE)) {
            column = caseWhen();
        } else {
            column = binaryArithmetic();
        }
        if (tokenSequence.equalThenAdvance(TokenType.AS)) {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            return new ColumnRep(column, token);
        }
        return new ColumnRep(column);
    }

    /**
     * "case" binaryArithmetic ("when" binaryArithmetic "then" binaryArithmetic)+ "else" binaryArithmetic "end"
     */
    private Curd caseWhen() {
        consume(TokenType.CASE, AstROErrorReporterDelegate.SELECT_caseWhen_PARSE);
        Curd value = binaryArithmetic();
        List<Curd> conditionList = new ArrayList<>();
        List<Curd> resultList = new ArrayList<>();

        while (tokenSequence.equalThenAdvance(TokenType.WHEN)) {
            conditionList.add(binaryArithmetic());
            consume(TokenType.THEN, AstROErrorReporterDelegate.SELECT_caseWhen_PARSE);
            resultList.add(binaryArithmetic());
        }
        Token token = tokenSequence.takeCur();
        Curd defaultResult = null;
        if (TokenType.ELSE.equals(token.getType())) {
            tokenSequence.advance();
            defaultResult = binaryArithmetic();
        }

        consume(TokenType.END, AstROErrorReporterDelegate.SELECT_caseWhen_PARSE);
        return new CaseWhen(value, conditionList, resultList, defaultResult);
    }


    /**
     * "from" tableRep (("left" | "right" | "cross")? ("join" | ",") tableRep ("on" logic)?)*
     */
    private Curd tableSeg() {
        if (!tokenSequence.curEqual(TokenType.FROM)) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_PARSE);
        }
        tokenSequence.advance();
        Curd left = tableRep();

        while (tokenSequence.curEqual(TokenType.JOIN,
                TokenType.COMMA, TokenType.LEFT, TokenType.RIGHT, TokenType.CROSS)) {

            Token qualifier = null;
            Token join = null;

            if (tokenSequence.curEqual(TokenType.LEFT, TokenType.RIGHT, TokenType.CROSS)) {
                qualifier = tokenSequence.takeCur();
                tokenSequence.advance();
            }

            if (tokenSequence.curEqual(TokenType.COMMA, TokenType.JOIN)) {
                join = tokenSequence.takeCur();
                tokenSequence.advance();
            }

            Curd right = tableRep();
            if (tokenSequence.equalThenAdvance(TokenType.ON)) {
                Curd logic = logic();
                left = new TableSeg(qualifier, left, join, right, logic);
            } else {
                left = new TableSeg(qualifier, left, join, right);
            }
        }
        if (left instanceof TableRep) {
            return new TableSeg(null, left, null, null, null);
        }
        return left;
    }


    /**
     * ("(" select ")" | IDENTIFIER) ("as" IDENTIFIER)?
     */
    private Curd tableRep() {
        boolean flag = false;
        Curd curd = null;
        if (checkSelectValue(tokenSequence)) {
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_tableRep_PARSE);
            curd = select();
            Select select = (Select) curd;
            select.setSelectType(SelectType.TABLE);
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_tableRep_PARSE);
            flag = true;
        } else if (tokenSequence.curEqual(TokenType.IDENTIFIER)) {
            Token table = tokenSequence.takeCur();
            tokenSequence.advance();
            curd = new Identifier(table);
        } else {
            error(AstROErrorReporterDelegate.SELECT_tableRep_PARSE);
        }

        Token alias = tokenSequence.takeCur();
        if (TokenType.IDENTIFIER.equals(alias.getType())) {
            tokenSequence.advance();
            return new TableRep(curd, alias);
        }

        // 不存在别名的场景
        // select 语句作为表时 需要存在 别名
        if (flag) {
            error(AstROErrorReporterDelegate.SELECT_tableRep_PARSE);
        }

        return new TableRep(curd);
    }


    /**
     * "where" logic
     */
    protected Curd whereSeg() {
        Curd curd = super.whereSeg();
        saveSingleSelectContext(curd, SingleSelectType.PRIMARY);
        return curd;
    }


    /**
     * "group" "by" IDENTIFIER ("," IDENTIFIER)* ("having" logic)?
     */
    private Curd groupBySeg() {
        consume(TokenType.GROUP, AstROErrorReporterDelegate.SELECT_groupBySeg_PARSE);
        consume(TokenType.BY, AstROErrorReporterDelegate.SELECT_groupBySeg_PARSE);
        List<Token> columnList = columnList();
        if (tokenSequence.equalThenAdvance(TokenType.HAVING)) {
            Curd logic = logic();
            return new GroupBySeg(columnList, logic);
        }
        return new GroupBySeg(columnList);
    }


    /**
     * "order" "by" IDENTIFIER ("desc" | "asc") ("," IDENTIFIER ("desc" | "asc"))*
     */
    private Curd orderBySeg() {
        consume(TokenType.ORDER, AstROErrorReporterDelegate.SELECT_orderBySeg_PARSE);
        consume(TokenType.BY, AstROErrorReporterDelegate.SELECT_orderBySeg_PARSE);
        List<OrderBySeg.OrderItem> columnList = new ArrayList<>();
        do {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            if (tokenSequence.curEqual(TokenType.ASC, TokenType.DESC)) {
                columnList.add(new OrderBySeg.OrderItem(token, tokenSequence.takeCur()));
                tokenSequence.advance();
            } else {
                columnList.add(new OrderBySeg.OrderItem(token));
            }
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return new OrderBySeg(columnList);
    }


    /**
     * "limit" INTEGER ("," INTEGER)?
     */
    private Curd limitSeg() {
        consume(TokenType.LIMIT, AstROErrorReporterDelegate.SELECT_limitSeg_PARSE);
        if (!tokenSequence.curEqual(TokenType.INTEGER)) {
            error(AstROErrorReporterDelegate.SELECT_limitSeg_PARSE);
        }
        Token token = tokenSequence.takeCur();
        tokenSequence.advance();
        if (tokenSequence.equalThenAdvance(TokenType.COMMA)) {
            if (!tokenSequence.curEqual(TokenType.INTEGER)) {
                error(AstROErrorReporterDelegate.SELECT_limitSeg_PARSE);
            }
            // 此时token是跳过的数量
            LimitSeg limitSeg = new LimitSeg(tokenSequence.takeCur(), token);
            tokenSequence.advance();
            return limitSeg;
        }
        return new LimitSeg(token);
    }


    /**
     * inCondition | existsCondition | comparison
     */
    protected Curd condition() {
        if (tokenSequence.curEqual(TokenType.EXISTS, TokenType.NOT)) {
            return existsCondition();
        } else if (tokenSequence.nextEqual(TokenType.NOT, TokenType.IN)) {
            return inCondition();
        }
        return comparison();
    }


    /**
     * IDENTIFIER ("not")? "in" "(" select | (primary (, primary)*) ")"
     */
    private Curd inCondition() {
        Token identifier = tokenSequence.takeCur();
        tokenSequence.advance();
        Token in = tokenSequence.takeCur();
        if (tokenSequence.curEqual(TokenType.NOT)) {
            tokenSequence.advance();
        }
        tokenSequence.advance();

        if (checkSelectValue(tokenSequence)) {
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_inCondition_PARSE);
            Select select = (Select) select();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_inCondition_PARSE);
            InCondition inCondition = new InCondition(in, identifier, select);
            select.setSelectType(SelectType.IN);
            return inCondition;
        }
        consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_inCondition_PARSE);
        List<Curd> ranges = new ArrayList<>();
        do {
            Curd primary = primary();
            if (primary instanceof Literal) {
                ranges.add(primary);
            } else {
                error(AstROErrorReporterDelegate.SELECT_inCondition_PARSE);
            }
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_inCondition_PARSE);
        return new InCondition(in, identifier, ranges);
    }


    /**
     * ("not")? "exists" "(" singleSelect ")"
     */
    private Curd existsCondition() {
        Token exists = tokenSequence.takeCur();
        if (tokenSequence.curEqual(TokenType.NOT)) {
            tokenSequence.advance();
        }
        tokenSequence.advance();
        consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_existsCondition_PARSE);
        Select select = (Select) select();
        consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_existsCondition_PARSE);
        ExistsCondition existsCondition = new ExistsCondition(exists, select);
        // 存select语句的山下文。
        select.setSelectType(SelectType.EXIST);

        return existsCondition;
    }


    /**
     * binaryArithmetic ((">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains") binaryArithmetic)?
     */
    @Override
    protected Curd comparison() {
        Curd curd = super.comparison();
        if (curd instanceof Comparison) {
            saveSingleSelectContext(curd, SingleSelectType.PRIMARY);
        }
        return curd;
    }


    /**
     * unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     */
    @Override
    protected Curd binaryArithmetic() {
        Curd curd = super.binaryArithmetic();
        if (curd instanceof BinaryArithmetic) {
            saveSingleSelectContext(curd, SingleSelectType.PRIMARY);
        }
        return curd;
    }


    /**
     * ("!"|"-") unaryArithmetic | primary
     */
    @Override
    protected Curd unaryArithmetic() {
        Curd curd = super.unaryArithmetic();
        if (curd instanceof UnaryArithmetic) {
            saveSingleSelectContext(curd, SingleSelectType.PRIMARY);
        }
        return curd;
    }


    /**
     * #{xxx} | ? | "true" | "false" | "null" | INTEGER | DECIMAL | STRING | IDENTIFIER | function | "(" singleSelect ")" | "(" logic ")" | groupFunction
     */
    @Override
    protected Curd primary() {
        Token token = tokenSequence.takeCur();
        if (checkGroupFunctionName(token)) {
            return groupFunction();
        }

        // select
        if (checkSelectValue(tokenSequence)) {
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_primary_PARSE);
            Select select = (Select) select();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_primary_PARSE);
            return select;
        }

        Curd primary = super.primary();
        if (primary != null) {
            return primary;
        }

        error(AstROErrorReporterDelegate.CURD_token_undefined_PARSE);
        return null;
    }


    /**
     * ("avg" | "max" | "min" | "count" | "sum", "flat") "(" IDENTIFIER ")"
     */
    protected Curd groupFunction() {
        Token method = tokenSequence.takeCur();
        tokenSequence.advance();
        consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.SELECT_groupFunction_PARSE);
        Curd curd;
        if (tokenSequence.curEqual(TokenType.STAR)) {
            curd = new Identifier(tokenSequence.takeCur());
            tokenSequence.advance();
        } else {
            curd = binaryArithmetic();
        }
        consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_groupFunction_PARSE);
        return new GroupFunction(method, curd);
    }

    protected static Set<TokenType> groupFunctionTypeSet = new HashSet<>();

    static {
        groupFunctionTypeSet.add(TokenType.MAX);
        groupFunctionTypeSet.add(TokenType.MIN);
        groupFunctionTypeSet.add(TokenType.FLAT);
        groupFunctionTypeSet.add(TokenType.COUNT);
        groupFunctionTypeSet.add(TokenType.AVG);
        groupFunctionTypeSet.add(TokenType.SUM);
    }

    protected boolean checkGroupFunctionName(Token token) {
        return groupFunctionTypeSet.contains(token.getType());
    }

    private boolean checkSelectValue(TokenSequence tokenSequence) {
        Token cur = tokenSequence.takeCur();
        Token next = tokenSequence.takeNext();
        return TokenType.LEFT_PAREN.equals(cur.getType()) && TokenType.SELECT.equals(next.getType());
    }

}

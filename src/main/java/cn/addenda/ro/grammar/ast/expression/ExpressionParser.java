package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.AbstractCurdParser;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionGrammarValidator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * logic               ->  condition (("or" | "and") condition)*
 * condition           ->  comparison
 * comparison          ->  binaryArithmetic ((">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot) binaryArithmetic)?
 * isNot               ->  "is" ("not")?
 * binaryArithmetic    ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
 * unaryArithmetic     ->  ("!"|"-") unaryArithmetic | primary
 * primary             ->  #{xxx} | ? | "true" | "false" | "null" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function
 * grouping            ->  "(" logic ")"
 * <p>
 * function            ->  functionName "(" functionParameter? ("," functionParameter)* ")"
 * functionParameter   ->  logic | timeInterval | timeUnit | function
 * timeInterval        ->  "interval" INTEGER IDENTIFIER
 * timeUnit            ->  IDENTIFIER "from" primary
 * <p>
 * whereSeg            ->  "where" logic
 * assignmentList      ->  (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
 * columnList	       ->  IDENTIFIER ("," IDENTIFIER)*
 *
 * @author addenda
 * @datetime 2021/4/5 12:11
 */
public class ExpressionParser extends AbstractCurdParser {

    public ExpressionParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    @Override
    public Curd parse() {
        Curd logic = logic();
        if (logic == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_logic_VALIDATION);
            return null;
        }
        logic.accept(new ExpressionGrammarValidator(null, this.errorReporterDelegate));
        return logic;
    }

    /**
     * condition (("or" | "and") condition)*
     */
    protected Curd logic() {
        Curd left = condition();
        while (tokenSequence.curEqual(TokenType.OR, TokenType.AND)) {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd right = condition();
            left = new Logic(left, token, right);
        }
        if (left == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_logic_PARSE);
            return null;
        }
        return left;
    }


    /**
     * comparison
     */
    protected Curd condition() {
        return comparison();
    }


    /**
     * binaryArithmetic (comparisonSymbol binaryArithmetic)?
     */
    protected Curd comparison() {
        Curd left = binaryArithmetic();
        Curd token = comparisonSymbol();
        if (token != null) {
            Curd right = binaryArithmetic();
            return new Comparison(left, token, right);
        }
        if (left == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_comparison_PARSE);
            return null;
        }
        return left;
    }


    /**
     * ">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot
     */
    private Curd comparisonSymbol() {
        if (tokenSequence.curEqual(TokenType.LIKE, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS,
                TokenType.LESS_EQUAL, TokenType.BANG_EQUAL, TokenType.EQUAL, TokenType.CONTAINS)) {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            return new Identifier(token);
        } else if (tokenSequence.curEqual(TokenType.IS)) {
            Token isToken = tokenSequence.takeCur();
            tokenSequence.advance();
            Token notToken = null;
            if (tokenSequence.curEqual(TokenType.NOT)) {
                notToken = tokenSequence.takeCur();
                tokenSequence.advance();
            }
            return new IsNot(isToken, notToken);
        }
        return null;
    }


    /**
     * unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     */
    protected Curd binaryArithmetic() {
        Curd expression = unaryArithmetic();
        while (tokenSequence.curEqual(TokenType.PLUS, TokenType.MINUS,
                TokenType.STAR, TokenType.SLASH)) {
            Token operator = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd right = unaryArithmetic();
            expression = new BinaryArithmetic(expression, operator, right);
        }
        if (expression == null) {
            error(AstROErrorReporterDelegate.EXPRESSION_binaryArithmetic_PARSE);
            return null;
        }
        return expression;
    }


    /**
     * ("!"|"-") unaryArithmetic | primary
     */
    protected Curd unaryArithmetic() {
        // 不一定合法
        if (tokenSequence.curEqual(TokenType.BANG, TokenType.MINUS)) {
            Token operator = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd curd = unaryArithmetic();
            return new UnaryArithmetic(operator, curd);
        }
        return primary();
    }


    /**
     * #{xxx} | ? | "true" | "false" | "null" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function
     */
    protected Curd primary() {
        if (tokenSequence.equalThenAdvance(TokenType.HASH_MARK_PLACEHOLDER, TokenType.PARAMETER,
                TokenType.TRUE, TokenType.FALSE, TokenType.NULL, TokenType.INTEGER, TokenType.DECIMAL, TokenType.STRING)) {
            return new Literal(tokenSequence.takePre());
        }

        // "(" logic ")"
        if (tokenSequence.equalThenAdvance(TokenType.LEFT_PAREN)) {
            Curd curd = logic();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_primary_PARSE);
            return new Grouping(curd);
        }

        // IDENTIFIER
        if (tokenSequence.curEqual(TokenType.IDENTIFIER)) {
            Token current = tokenSequence.takeCur();
            Token next = tokenSequence.takeNext();
            if (checkFunction(current, next)) {
                return function();
            }
            tokenSequence.advance();
            return new Identifier(current);
        }
        return null;
    }


    /**
     * functionName "(" functionParameter? ("," functionParameter)* ")"
     */
    protected Curd function() {
        Token method = tokenSequence.takeCur();
        tokenSequence.advance();
        consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.EXPRESSION_function_PARSE);

        // 没有参数的情况
        if (tokenSequence.equalThenAdvance(TokenType.RIGHT_PAREN)) {
            return new Function(method);
        }

        // 有参数的情况
        ArrayList<Curd> parameterList = new ArrayList<>();
        do {
            parameterList.add(functionParameter());
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.EXPRESSION_function_PARSE);
        Function function = new Function(method, parameterList);
        doFunctionStaticCheck(function);
        return function;
    }


    /**
     * logic | timeInterval | timeUnit | function
     */
    protected Curd functionParameter() {
        Curd curd;
        Token current = tokenSequence.takeCur();
        Token next = tokenSequence.takeNext();
        if (checkFunction(current, next)) {
            curd = function();
        } else if (TokenType.INTERVAL.equals(current.getType())) {
            curd = timeInterval();
        } else if (checkTimeUnit(current, next)) {
            curd = timeUnit();
        } else {
            curd = logic();
        }
        return curd;
    }


    /**
     * "interval" INTEGER IDENTIFIER
     */
    protected Curd timeInterval() {
        consume(TokenType.INTERVAL, AstROErrorReporterDelegate.EXPRESSION_timeInterval_PARSE);

        Token time;
        Token token;

        if (tokenSequence.curEqual(TokenType.INTEGER)) {
            time = tokenSequence.takeCur();
            tokenSequence.advance();
        } else {
            error(AstROErrorReporterDelegate.EXPRESSION_timeInterval_PARSE);
            return null;
        }

        if (tokenSequence.curEqual(TokenType.IDENTIFIER)) {
            token = tokenSequence.takeCur();
            tokenSequence.advance();
        } else {
            error(AstROErrorReporterDelegate.EXPRESSION_timeInterval_PARSE);
            return null;
        }
        Long interval = null;
        Object value = time.getLiteral();
        if (value instanceof Integer) {
            interval = Long.parseLong(value.toString());
        } else if (value instanceof Long) {
            interval = (Long) value;
        }

        return new TimeInterval(token, interval);
    }


    /**
     * IDENTIFIER "from" primary
     */
    private Curd timeUnit() {
        Token unit = tokenSequence.takeCur();
        tokenSequence.advance();
        consume(TokenType.FROM, AstROErrorReporterDelegate.EXPRESSION_timeUnit_PARSE);
        Curd curd = primary();

        return new TimeUnit(unit, curd);
    }


    /**
     * "where" logic
     */
    protected Curd whereSeg() {
        consume(TokenType.WHERE, AstROErrorReporterDelegate.EXPRESSION_whereSeg_PARSE);
        return new WhereSeg(logic());
    }


    /**
     * (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
     */
    protected Curd assignmentList() {
        List<AssignmentList.Entry> entryList = new ArrayList<>();
        do {
            Token token = consume(TokenType.IDENTIFIER, AstROErrorReporterDelegate.EXPRESSION_assignmentList_PARSE);
            consume(TokenType.EQUAL, AstROErrorReporterDelegate.EXPRESSION_assignmentList_PARSE);
            Curd curd = binaryArithmetic();
            entryList.add(new AssignmentList.Entry(token, curd));
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return new AssignmentList(entryList);
    }


    /**
     * IDENTIFIER ("," IDENTIFIER)*
     */
    protected List<Token> columnList() {
        List<Token> tokenList = new ArrayList<>();
        do {
            consume(TokenType.IDENTIFIER, AstROErrorReporterDelegate.EXPRESSION_columnList_PARSE);
            tokenList.add(tokenSequence.takePre());
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return tokenList;
    }


    protected Token consume(TokenType tokenType, int errorCode) {
        Token consume = tokenSequence.consume(tokenType);
        if (consume == null) {
            error(errorCode);
            return null;
        }
        return consume;
    }


}

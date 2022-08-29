package cn.addenda.ro.grammar.lexical.token;

/**
 * @author addenda
 * @datetime 2021/2/20 17:09
 */
public enum TokenType {

    /**
     * Single-character tokens.
     */
    // (
    LEFT_PAREN,
    // )
    RIGHT_PAREN,
    // ,
    COMMA,
    // -
    MINUS,
    // +
    PLUS,
    // /
    SLASH,
    // *
    STAR,

    /**
     * One or two character tokens.
     */
    // !
    BANG,
    // !=
    BANG_EQUAL,
    // =
    EQUAL,
    // >
    GREATER,
    // >=
    GREATER_EQUAL,
    // <
    LESS,
    // <=
    LESS_EQUAL,


    /**
     * LITERAL
     */
    IDENTIFIER, STRING, INTEGER, DECIMAL, HASH_MARK_PLACEHOLDER, PARAMETER,

    /**
     * PRIMARY
     */
    TRUE, NULL, FALSE,

    /**
     * LOGIC
     */
    AND, OR,

    /**
     * ORDER
     */
    ORDER, ASC, DESC,

    /**
     * GROUP
     */
    GROUP, FLAT, SUM, COUNT, MIN, MAX, AVG,

    /**
     * CONDITION
     */
    WHERE, HAVING, EXISTS, IN, IS, LIKE, CONTAINS,

    /**
     * JOIN
     */
    RIGHT, CROSS, LEFT, JOIN,

    /**
     * SET OPERATION
     */
    CUNION, CMINUS, CINTERSECT, CEXCEPT, ALL,

    /**
     * RESULT QUALIFICATION
     */
    DISTINCT,

    /**
     * SELECT
     */
    SELECT, AS, BY, NOT,

    /**
     * INSERT
     */
    INSERT, INTO, IGNORE, DUPLICATE, KEY, VALUES,

    /**
     * DELETE
     */
    DELETE,

    /**
     * UPDATE
     */
    UPDATE,

    /**
     * CASE WHEN
     */
    CASE, WHEN, THEN, ELSE, END,

    /**
     * LIMIT
     */
    LIMIT, OFFSET,

    /**
     * COMMON
     */
    FROM, SET, ON,

    /**
     * TIME
     */
    INTERVAL,

    /**
     * SELECT 锁
     */
    LOCK, SHARE, MODE, FOR,

    /**
     * END.
     */
    EOF,

}
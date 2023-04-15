package cn.addenda.ro.grammar.lexical.token;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.DeepCloneable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2021/2/20 17:03
 */
public class Token implements ROError, DeepCloneable<Token> {

    public static final Set<Class<?>> LITERAL_CLASS_SET = new HashSet<>();

    private final TokenType type;
    /**
     * 存 INTEGER、 DECIMAL、STRING、TRUE、FALSE、NULL
     */
    private Object literal;

    private boolean graveFg;

    private int index = -1;

    public Token(TokenType type, Object literal) {
        this.type = type;
        this.literal = literal;
        this.graveFg = false;
        if (literal != null) {
            LITERAL_CLASS_SET.add(literal.getClass());
        }
    }

    public Token(TokenType type, Object literal, int index) {
        this.type = type;
        this.literal = literal;
        this.index = index;
        this.graveFg = false;
        if (literal != null) {
            LITERAL_CLASS_SET.add(literal.getClass());
        }
    }

    public Token(TokenType type, Object literal, int index, boolean graveFg) {
        this.type = type;
        this.literal = literal;
        this.index = index;
        this.graveFg = graveFg;
        if (literal != null) {
            LITERAL_CLASS_SET.add(literal.getClass());
        }
    }


    @Override
    public String toString() {
        return "Token {" +
                "type: " + type +
                ", literal: \"" + literal +
                "\"}";
    }

    public TokenType getType() {
        return type;
    }

    public Object getLiteral() {
        return literal;
    }

    public Object printLiteral() {
        if (isGraveFg()) {
            return "`" + literal + "`";
        }
        return literal;
    }

    public int getIndex() {
        return index;
    }

    public boolean isGraveFg() {
        return graveFg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return graveFg == token.graveFg && type == token.type && Objects.equals(literal, token.literal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, literal, graveFg);
    }

    @Override
    public Token deepClone() {
        return new Token(this.getType(), this.getLiteral(), this.getIndex(), graveFg);
    }

    @Override
    public Token deepClone(boolean detectAstMetaData) {
        throw new UnsupportedOperationException();
    }

}
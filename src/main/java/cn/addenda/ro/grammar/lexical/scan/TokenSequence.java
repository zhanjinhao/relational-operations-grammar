package cn.addenda.ro.grammar.lexical.scan;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.DeepCloneable;
import cn.addenda.ro.grammar.lexical.Sequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.lexical.token.TokenTypeLexemeMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @datetime 2021/2/23 15:35
 */
public class TokenSequence extends Sequence<List<Token>, Token> implements ROError, DeepCloneable<TokenSequence> {

    public TokenSequence() {
        super(new ArrayList<>(), new Token(TokenType.EOF, null));
    }

    public List<Token> getSource() {
        return source;
    }

    public void addItem(Token token) {
        source.add(token);
        length++;
    }

    @Override
    protected int length() {
        return source.size();
    }

    @Override
    protected boolean equalSequenceItem(Token c1, Token c2) {
        return c1.getType() == c2.getType();
    }

    @Override
    protected Token indexOf(int index) {
        return source.get(index);
    }

    public Token consume(TokenType type) {
        if (curEqual(type)) {
            Token token = takeCur();
            advance();
            return token;
        }
        return null;
    }

    public boolean curEqual(TokenType... types) {
        for (TokenType type : types) {
            if (curEqual(wrapTokenType(type))) {
                return true;
            }
        }
        return false;
    }

    public boolean nextEqual(TokenType... types) {
        for (TokenType type : types) {
            if (nextEqual(wrapTokenType(type))) {
                return true;
            }
        }
        return false;
    }

    private Token wrapTokenType(TokenType tokenType) {
        return new Token(tokenType, TokenTypeLexemeMapping.getLexeme(tokenType));
    }

    public boolean equalThenAdvance(TokenType... types) {
        for (TokenType type : types) {
            if (equalThenAdvance(wrapTokenType(type))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return source.stream().filter(token -> token.getLiteral() != null).map(item -> {
            TokenType type = item.getType();
            if (TokenType.STRING.equals(type)) {
                return "'" + item.getLiteral().toString() + "'";
            }
            return item.getLiteral().toString();
        }).collect(Collectors.joining(" "));
    }

    @Override
    public TokenSequence deepClone() {
        TokenSequence tokenSequence = new TokenSequence();
        source.forEach(token -> tokenSequence.addItem(token.deepClone()));
        return tokenSequence;
    }

    @Override
    protected boolean doEquals(List<Token> source) {
        int size = source.size();
        for (int i = 0; i < size; i++) {
            if (!this.source.get(i).equals(source.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int doHashCode() {
        return Arrays.hashCode(source.toArray());
    }

}
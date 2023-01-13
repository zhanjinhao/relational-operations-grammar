package cn.addenda.ro.grammar.lexical.scan;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.lexical.ScanErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.Scanner;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.lexical.token.TokenTypeLexemeMapping;

/**
 * @author addenda
 * @datetime 2021/2/23 19:58
 */
public abstract class AbstractScanner implements Scanner<TokenSequence>, ROErrorReporter {

    private final ROErrorReporter roErrorReporter = new ScanErrorReporterDelegate();

    private int index = 0;

    protected TokenSequence tokenSequence;

    protected CharSequence charSequence;

    protected AbstractScanner(String source) {
        charSequence = new CharSequence(source);
        tokenSequence = new TokenSequence();
    }

    @Override
    public TokenSequence scanTokens() {
        while (!charSequence.isAtEnd()) {
            charSequence.recordLast();
            doScanTokens();
        }
        addToken(TokenType.EOF, null);
        return tokenSequence;
    }

    /**
     * 扫描 Token
     */
    protected abstract void doScanTokens();

    protected void addToken(TokenType type) {
        addToken(type, TokenTypeLexemeMapping.getLexeme(type));
    }

    protected void addToken(TokenType type, Object literal) {
        tokenSequence.addItem(new Token(type, literal, index));
        index++;
    }

    protected void addToken(TokenType type, Object literal, boolean graveFg) {
        tokenSequence.addItem(new Token(type, literal, index, graveFg));
        index++;
    }

    @Override
    public void error(int errorCode) {
        roErrorReporter.error(errorCode);
    }

    @Override
    public void error(int errorCode, ROError roError) {
        roErrorReporter.error(errorCode, roError);
    }
}

package cn.addenda.ro.grammar.lexical.scan;

import cn.addenda.ro.grammar.lexical.ScanErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.lexical.token.TokenTypeLexemeMapping;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author addenda
 * @datetime 2021/2/20 17:07
 */
public class DefaultScanner extends AbstractScanner {

    public DefaultScanner(String source) {
        super(source);
    }

    @Override
    protected void doScanTokens() {
        char c = charSequence.takeCur();
        charSequence.advance();
        switch (c) {
            case '?':
                addToken(TokenType.PARAMETER);
                break;
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '=':
                addToken(TokenType.EQUAL);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case '!':
                addToken(charSequence.equalThenAdvance('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '<':
                addToken(charSequence.equalThenAdvance('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(charSequence.equalThenAdvance('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case ' ':
            case '\r':
            case '\t':
            case '\n':
                // Ignore whitespace.
                break;
            case '\'':
                string();
                break;
            case '#':
                hashMarkParameter();
                break;
            default:
                if (CharUtil.isDigit(c)) {
                    number();
                } else if (CharUtil.isAlpha(c)) {
                    identifier();
                } else {
                    error(ScanErrorReporterDelegate.LEXICAL_UNEXPECTED_CHAR);
                }
                break;
        }
    }

    private void hashMarkParameter() {
        if (!charSequence.curEqual('{')) {
            error(ScanErrorReporterDelegate.LEXICAL_HASHMARK_FORMAT);
        }
        while (!charSequence.curEqual('}')) {
            charSequence.advance();
        }
        if (charSequence.isAtEnd()) {
            error(ScanErrorReporterDelegate.LEXICAL_HASHMARK_FORMAT);
        }
        charSequence.advance();
        addToken(TokenType.HASH_MARK_PLACEHOLDER, charSequence.curLiteral());
    }

    private void string() {
        while (!charSequence.isAtEnd() && !charSequence.curEqual('\'')) {
            charSequence.advance();
        }

        if (charSequence.isAtEnd()) {
            error(ScanErrorReporterDelegate.LEXICAL_STRING);
        }

        charSequence.advance();
        String originalStr = charSequence.curLiteral();
        String value = originalStr.substring(1, originalStr.length() - 1);
        addToken(TokenType.STRING, value);
    }

    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    private static final BigDecimal MIN_DOUBLE = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

    private void number() {
        while (charSequence.curTest(CharUtil::isDigit)) {
            charSequence.advance();
        }
        // Look for a fractional part.
        if (charSequence.curEqual('.') && charSequence.nextTest(CharUtil::isDigit)) {
            // Consume the "."
            charSequence.advance();
            while (charSequence.curTest(CharUtil::isDigit)) {
                charSequence.advance();
            }
        } else if (charSequence.curEqual('L') && !charSequence.nextTest(CharUtil::isDigit)) {
            charSequence.advance();
        }

        // 处理数字的类型
        String s = charSequence.curLiteral();
        if (s.contains(".")) {
            BigDecimal bigDecimal = new BigDecimal(s);
            if (bigDecimal.compareTo(MIN_DOUBLE) > 0 && bigDecimal.compareTo(MAX_DOUBLE) < 0) {
                addToken(TokenType.NUMBER, Double.parseDouble(s));
            } else {
                addToken(TokenType.NUMBER, bigDecimal);
            }
        } else {
            BigInteger bigInteger = new BigInteger(s);
            if (bigInteger.compareTo(MIN_LONG) > 0 && bigInteger.compareTo(MAX_LONG) < 0) {
                addToken(TokenType.NUMBER, Long.parseLong(s));
            } else {
                addToken(TokenType.NUMBER, bigInteger);
            }
        }
    }

    private void identifier() {
        while (charSequence.curTest(CharUtil::isAlphaNumericDotStar)) {
            charSequence.advance();
        }
        String text = charSequence.curLiteral();
        TokenType type = TokenTypeLexemeMapping.getTokenType(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type, text);
    }


//    @Override
//    protected void addToken(TokenType type, Object literal) {
//        if (literal.equals("left")) {
//            literal = "left join";
//        } else if (literal.equals("right")) {
//            literal = "right join";
//        } else if (literal.equals("outer")) {
//            literal = "outer join";
//        }
//        super.addToken(type, literal);
//    }


    private static class CharUtil {
        public static boolean isAlpha(char c) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
        }

        public static boolean isAlphaNumericDotStar(char c) {
            return isAlpha(c) || isDigit(c) || isDot(c) || isStar(c);
        }

        public static boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }

        public static boolean isDot(char c) {
            return c == '.';
        }

        public static boolean isStar(char c) {
            return c == '*';
        }

    }

}
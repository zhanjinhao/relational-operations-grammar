package cn.addenda.ro.grammar.lexical;

import cn.addenda.ro.error.reporter.AbstractROErrorReporterDelegate;

/**
 * @author addenda
 * @datetime 2022/1/26 17:12
 */
public class ScanErrorReporterDelegate extends AbstractROErrorReporterDelegate {

    @Override
    protected void fillErrorMsg() {

        addErrorMsg(LEXICAL_UNEXPECTED_CHAR, LEXICAL_UNEXPECTED_CHAR_MSG);
        addErrorMsg(LEXICAL_HASHMARK_FORMAT, LEXICAL_HASHMARK_FORMAT_MSG);
        addErrorMsg(LEXICAL_STRING, LEXICAL_STRING_MSG);
        addErrorMsg(LEXICAL_GRAVE_ACCENT, LEXICAL_GRAVE_ACCENT_MSG);

    }

    @Override
    public void error(int errorCode) {
        throw new LexicalException(errorCode, getErrorMsg(errorCode));
    }

    public static final int LEXICAL_UNEXPECTED_CHAR = 10001;
    public static final String LEXICAL_UNEXPECTED_CHAR_MSG = "Unexpected character. Can not recognize.";

    public static final int LEXICAL_HASHMARK_FORMAT = 10002;
    public static final String LEXICAL_HASHMARK_FORMAT_MSG = "The hash mark format is: '#{xxx}'.";

    public static final int LEXICAL_STRING = 10003;
    public static final String LEXICAL_STRING_MSG = "Unterminated string. Expect : '''";

    public static final int LEXICAL_GRAVE_ACCENT = 10004;
    public static final String LEXICAL_GRAVE_ACCENT_MSG = "Unterminated identifier. Expect : '`'";


}

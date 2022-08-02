package cn.addenda.ro.grammar.lexical.scan;

import cn.addenda.ro.grammar.lexical.Sequence;

/**
 * @author addenda
 * @datetime 2021/2/23 15:07
 */
public class CharSequence extends Sequence<String, Character> {

    public CharSequence(String source) {
        super(source, '\0');
    }

    public String curLiteral() {
        return source.substring(last + 1, current);
    }

    @Override
    protected int length() {
        return source.length();
    }

    @Override
    protected boolean equalSequenceItem(Character c1, Character c2) {
        return c1.equals(c2);
    }

    @Override
    protected Character indexOf(int index) {
        return source.charAt(index);
    }

    @Override
    public String toString() {
        return "source : " + source + ", current character is " + takeCur();
    }

}

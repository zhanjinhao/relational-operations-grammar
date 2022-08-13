package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class LimitSeg extends Curd {

    // 需要取的值
    private Token num;

    // 跳过的数量
    private Token offset;

    public LimitSeg(Token num, Token offset) {
        super(new SingleSelectAstMetaData());
        this.num = num;
        this.offset = offset;
    }

    public LimitSeg(Token num) {
        super(new SingleSelectAstMetaData());
        this.num = num;
        this.offset = null;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitLimitSeg(this);
    }

    public Token getNum() {
        return num;
    }

    public Token getOffset() {
        return offset;
    }

}

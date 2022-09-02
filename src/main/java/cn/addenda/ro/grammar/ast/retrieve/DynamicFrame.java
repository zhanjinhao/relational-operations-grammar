package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2022/8/31 19:15
 */
public class DynamicFrame extends Curd {

    /**
     * 可选值："rows" | "range"
     */
    private Token type;

    private Curd frameRange;

    public DynamicFrame(Token type, Curd frameRange) {
        super(new SingleSelectAstMetaData());
        this.type = type;
        this.frameRange = frameRange;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitDynamicFrame(this);
    }

    public Token getType() {
        return type;
    }

    public Curd getFrameRange() {
        return frameRange;
    }
}

package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author addenda
 * @datetime 2022/8/31 19:09
 */
public class FrameEdge extends Curd {

    /**
     * 可选值：INTEGER | "unbounded" | "current"
     */
    private Token edge;

    /**
     * 可选值："preceding" | "following" | "row"
     */
    private Token towards;

    public FrameEdge(Token edge, Token towards) {
        super(new SingleSelectAstMetaData());
        this.edge = edge;
        this.towards = towards;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitFrameEdge(this);
    }

    public Token getEdge() {
        return edge;
    }

    public Token getTowards() {
        return towards;
    }
}

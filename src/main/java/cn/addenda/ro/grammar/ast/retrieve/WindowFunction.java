package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/31 19:23
 */
public class WindowFunction extends Curd {

    private Token method;

    private List<Curd>  parameterList;

    private Curd window;

    public WindowFunction(Token method, List<Curd>  parameterList, Curd window) {
        super(new SingleSelectAstMetaData());
        this.method = method;
        this.parameterList = parameterList;
        this.window = window;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitWindowFunction(this);
    }

    public Token getMethod() {
        return method;
    }

    public List<Curd> getParameterList() {
        return parameterList;
    }

    public Curd getWindow() {
        return window;
    }
}

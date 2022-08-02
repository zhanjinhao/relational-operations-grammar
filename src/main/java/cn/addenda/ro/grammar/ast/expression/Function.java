package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/3/29 20:21
 */
public class Function extends Curd implements ROError {

    private Token method;

    private List<Curd> parameterList;

    public Function(Token method, List<Curd> parameterList) {
        this.method = method;
        this.parameterList = parameterList;
    }

    public Function(Token method) {
        this.method = method;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitFunction(this);
    }

    public Token getMethod() {
        return method;
    }

    public List<Curd> getParameterList() {
        return parameterList;
    }
}

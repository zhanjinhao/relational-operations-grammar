package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author addenda
 * @datetime 2021/3/3
 */
public class Comparison extends Binary {

    private Curd comparisonSymbol;

    public Comparison(Curd leftCurd, Curd comparisonSymbol,
                      Curd rightCurd) {
        super(leftCurd, null, rightCurd);
        this.comparisonSymbol = comparisonSymbol;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitComparison(this);
    }

    public Curd getComparisonSymbol() {
        return comparisonSymbol;
    }
}

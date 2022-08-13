package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/8/15 21:23
 */
public class CaseWhen extends Curd {

    private Curd value;

    private List<Curd> conditionList;

    private List<Curd> resultList;

    private Curd defaultValue;

    public CaseWhen(Curd value, List<Curd> conditionList, List<Curd> resultList, Curd defaultValue) {
        super(new SingleSelectAstMetaData());
        this.value = value;
        this.conditionList = conditionList;
        this.resultList = resultList;
        this.defaultValue = defaultValue;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitCaseWhen(this);
    }

    public Curd getValue() {
        return value;
    }

    public Curd getDefaultValue() {
        return defaultValue;
    }

    public List<Curd> getConditionList() {
        return conditionList;
    }

    public List<Curd> getResultList() {
        return resultList;
    }

}

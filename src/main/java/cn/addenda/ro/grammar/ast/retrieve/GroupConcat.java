package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;
import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/29 17:52
 */
public class GroupConcat extends Curd {

    /**
     * distinct
     */
    private Token modifier;
    private List<Curd> resultList;
    private List<Curd> orderItemList;
    private String separator;

    public GroupConcat(Token modifier, List<Curd> resultList, List<Curd> orderItemList, String separator) {
        super(new SingleSelectAstMetaData());
        this.modifier = modifier;
        this.resultList = resultList;
        this.orderItemList = orderItemList;
        this.separator = separator;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitGroupConcat(this);
    }

    public Token getModifier() {
        return modifier;
    }

    public List<Curd> getResultList() {
        return resultList;
    }

    public List<Curd> getOrderItemList() {
        return orderItemList;
    }

    public String getSeparator() {
        return separator;
    }

}

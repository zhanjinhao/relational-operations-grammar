package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @Date 2021/4/5 21:02
 */
public class InsertValuesRep extends Curd {

    private List<Token> columnList;
    private List<List<Curd>> curdListList;

    public InsertValuesRep(List<Token> columnList, List<List<Curd>> curdListList) {
        super(new InsertAstMetaData());
        this.columnList = columnList;
        this.curdListList = curdListList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsertValuesRep(this);
    }

    public List<Token> getColumnList() {
        return columnList;
    }

    public List<List<Curd>> getCurdListList() {
        return curdListList;
    }
}

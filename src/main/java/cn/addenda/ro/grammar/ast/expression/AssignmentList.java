package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/4/6
 */
public class AssignmentList extends Curd {

    private List<Entry> entryList;

    public AssignmentList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitAssignmentList(this);
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    public static class Entry {
        private Token column;
        private Curd value;
        public Entry(Token column, Curd value) {
            this.column = column;
            this.value = value;
        }
        public Token getColumn() {
            return column;
        }
        public void setColumn(Token column) {
            this.column = column;
        }
        public Curd getValue() {
            return value;
        }
        public void setValue(Curd value) {
            this.value = value;
        }
    }


}

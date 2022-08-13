package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.expression.Curd;

import java.util.*;

/**
 * 一个 AstMetaData 对应着一个VIEW，描述了它引用的条件列和返回列，以及在整个AST中的位置。
 *
 * @author addenda
 * @datetime 2022/1/4 22:03
 */
public class AstMetaData {

    public static final String UNDETERMINED_TABLE = "UNDETERMINED";

    private Curd curd;

    private AstMetaData parent;

    // INSERT, DELETE, SELECT, UPDATE 为 CURD 最顶层的结构
    // 如果遍历树的时候遇到了这四个语法（事实上只有SELECT），认为其是当前AST的孩子
    private final List<AstMetaData> children = new ArrayList<>();

    protected final Map<String, List<String>> conditionColumnReference = new HashMap<>();

    public AstMetaData() {
        conditionColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new ArrayList<>());
    }

    public void putUndeterminedConditionColumn(String column) {
        int i = column.indexOf(".");
        if (i == -1) {
            conditionColumnReference.get(UNDETERMINED_TABLE).add(column);
        } else if (i == column.length()) {
            // error
        } else {
            // condition要存表存字段
            String tableName = column.substring(0, i);
            conditionColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
            conditionColumnReference.get(tableName).add(column);

            // 其他的引用表需要建出来
            createTable(tableName);
        }
    }

    /**
     * 将astMetaData的conditionColumnMap合并到当前对象中
     */
    public void mergeColumnReference(AstMetaData astMetaData) {
        AstMetaDataHelper.mergeColumnReference(astMetaData.getConditionColumnReference(), conditionColumnReference);
    }

    public Curd getCurd() {
        return curd;
    }

    public void setCurd(Curd curd) {
        this.curd = curd;
    }

    public AstMetaData getParent() {
        return parent;
    }

    public void setParent(AstMetaData parent) {
        this.parent = parent;
    }

    public List<AstMetaData> getChildren() {
        return children;
    }

    public void addChild(AstMetaData astMetaData) {
        children.add(astMetaData);
    }

    public Map<String, List<String>> getConditionColumnReference() {
        return conditionColumnReference;
    }

    @Override
    public String toString() {
        return "AstMetaData{" +
                "curd=" + curd +
                ", conditionColumnMap=" + conditionColumnReference +
                ", conditionChildren=" + children +
                '}';
    }

    public void createTable(String tableName) {
        conditionColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
    }

    public void createTable(Map<String, List<String>> columnReference) {
        doCreateTable(columnReference, conditionColumnReference);
    }

    protected void doCreateTable(Map<String, List<String>> columnReferenceForSaveTable, Map<String, List<String>> columnReference) {
        Set<String> tableNames = columnReferenceForSaveTable.keySet();
        for (String tableName : tableNames) {
            columnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
        }
    }

}

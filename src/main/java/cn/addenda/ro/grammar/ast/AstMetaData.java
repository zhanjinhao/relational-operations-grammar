package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

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

    // 存where部分的孩子
    private final List<AstMetaData> conditionChildren = new ArrayList<>();

    // 存表部分的子AstMetaData
    private final List<AstMetaData> tableChildren = new ArrayList<>();

    // 存Select语句的SingleSelect集合
    private final List<AstMetaData> subSegments = new ArrayList<>();

    // 存返回的值
    private final List<Token> resultColumnList = new ArrayList<>();

    private final Map<String, List<String>> conditionColumnReference = new HashMap<>();
    private final Map<String, List<String>> resultColumnReference = new HashMap<>();
    private final Map<String, List<String>> joinColumnReference = new HashMap<>();
    private final Map<String, List<String>> groupByColumnReference = new HashMap<>();
    private final Map<String, List<String>> orderByColumnReference = new HashMap<>();

    private Map<String, Curd> aliasTableMap = new HashMap<>();

    public AstMetaData() {
        conditionColumnReference.put(UNDETERMINED_TABLE, new ArrayList<>());
        resultColumnReference.put(UNDETERMINED_TABLE, new ArrayList<>());
        joinColumnReference.put(UNDETERMINED_TABLE, new ArrayList<>());
        groupByColumnReference.put(UNDETERMINED_TABLE, new ArrayList<>());
        orderByColumnReference.put(UNDETERMINED_TABLE, new ArrayList<>());
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
            if (!conditionColumnReference.containsKey(tableName)) {
                conditionColumnReference.put(tableName, new ArrayList<>());
            }
            conditionColumnReference.get(tableName).add(column);

            // resultColumnReference 要存表
            if (!resultColumnReference.containsKey(tableName)) {
                resultColumnReference.put(tableName, new ArrayList<>());
            }
            // joinColumnReference 要存表
            if (!joinColumnReference.containsKey(tableName)) {
                joinColumnReference.put(tableName, new ArrayList<>());
            }
            // groupByColumnReference 要存表
            if (!groupByColumnReference.containsKey(tableName)) {
                groupByColumnReference.put(tableName, new ArrayList<>());
            }
            // orderByColumnReference 要存表
            if (!orderByColumnReference.containsKey(tableName)) {
                orderByColumnReference.put(tableName, new ArrayList<>());
            }
        }

    }


    /**
     * 将astMetaData的conditionColumnMap合并到当前对象中
     */
    public void mergeColumnReference(AstMetaData astMetaData) {
        mergeConditionColumnReference(astMetaData.getConditionColumnReference());
        mergeResultColumnReference(astMetaData.getResultColumnReference());
        mergeJoinColumnReference(astMetaData.getJoinColumnReference());
        mergeGroupByColumnReference(astMetaData.getGroupByColumnReference());
        mergeOrderByColumnReference(astMetaData.getOrderByColumnReference());
    }

    /**
     * 将thatConditionColumnMap合并到当前对象中
     */
    public void mergeConditionColumnReference(Map<String, List<String>> thatConditionColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatConditionColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (this.conditionColumnReference.containsKey(key)) {
                this.conditionColumnReference.get(key).addAll(value);
            } else {
                this.conditionColumnReference.put(key, new ArrayList<>(value));
            }
        }
    }

    /**
     * 将thatResultColumnMap合并到当前对象中
     */
    public void mergeResultColumnReference(Map<String, List<String>> thatResultColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatResultColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (this.resultColumnReference.containsKey(key)) {
                this.resultColumnReference.get(key).addAll(value);
            } else {
                this.resultColumnReference.put(key, new ArrayList<>(value));
            }
        }
    }

    /**
     * 将thatResultColumnMap合并到当前对象中
     */
    public void mergeJoinColumnReference(Map<String, List<String>> thatJoinColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatJoinColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (this.joinColumnReference.containsKey(key)) {
                this.joinColumnReference.get(key).addAll(value);
            } else {
                this.joinColumnReference.put(key, new ArrayList<>(value));
            }
        }
    }

    /**
     * 将thatGroupByColumnMap合并到当前对象中
     */
    public void mergeGroupByColumnReference(Map<String, List<String>> thatGroupByJoinColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatGroupByJoinColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (this.groupByColumnReference.containsKey(key)) {
                this.groupByColumnReference.get(key).addAll(value);
            } else {
                this.groupByColumnReference.put(key, new ArrayList<>(value));
            }
        }
    }

    /**
     * 将thatOrderByColumnMap合并到当前对象中
     */
    public void mergeOrderByColumnReference(Map<String, List<String>> thatOrderByJoinColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatOrderByJoinColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (this.orderByColumnReference.containsKey(key)) {
                this.orderByColumnReference.get(key).addAll(value);
            } else {
                this.orderByColumnReference.put(key, new ArrayList<>(value));
            }
        }
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

    public List<AstMetaData> getConditionChildren() {
        return conditionChildren;
    }

    public List<AstMetaData> getTableChildren() {
        return tableChildren;
    }

    public List<AstMetaData> getSubSegments() {
        return subSegments;
    }

    public List<Token> getResultColumnList() {
        return resultColumnList;
    }

    public Map<String, List<String>> getConditionColumnReference() {
        return conditionColumnReference;
    }

    public Map<String, List<String>> getResultColumnReference() {
        return resultColumnReference;
    }

    public Map<String, List<String>> getJoinColumnReference() {
        return joinColumnReference;
    }

    public Map<String, List<String>> getGroupByColumnReference() {
        return groupByColumnReference;
    }

    public Map<String, List<String>> getOrderByColumnReference() {
        return orderByColumnReference;
    }


    public Map<String, Curd> getAliasTableMap() {
        return aliasTableMap;
    }

    public void setAliasTableMap(Map<String, Curd> aliasTableMap) {
        this.aliasTableMap = aliasTableMap;
    }

    public void sortMetaData() {
        doSortMetaData(conditionColumnReference, aliasTableMap);
        doSortMetaData(resultColumnReference, aliasTableMap);
        doSortMetaData(joinColumnReference, aliasTableMap);
        doSortMetaData(groupByColumnReference, aliasTableMap);
        doSortMetaData(orderByColumnReference, aliasTableMap);

    }

    private void doSortMetaData(Map<String, List<String>> columnReference, Map<String, Curd> aliasTableMap) {
        // 需要将表建到引用中去
        Set<String> aliasTableEntries = aliasTableMap.keySet();
        for (String tableName : aliasTableEntries) {
            if (!columnReference.containsKey(tableName)) {
                columnReference.put(tableName, new ArrayList<>());
            }
        }
        // 只有一个表时，认为字段都是这个表的
        if (columnReference.size() == 2) {
            String tableName = null;
            Set<String> columnReferenceEntries = columnReference.keySet();
            for (String item : columnReferenceEntries) {
                if (!UNDETERMINED_TABLE.equals(item)) {
                    tableName = item;
                }
            }
            List<String> strings = columnReference.get(UNDETERMINED_TABLE);
            for (String column : strings) {
                columnReference.get(tableName).add(column);
            }
            columnReference.get(UNDETERMINED_TABLE).clear();
        }
    }

    @Override
    public String toString() {
        return "AstMetaData{" +
                "curd=" + curd +
                ", resultColumnList=" + resultColumnList +
                ", conditionColumnMap=" + conditionColumnReference +
                ", resultColumnMap=" + resultColumnReference +
                ", curdTableMap=" + aliasTableMap +
                ", conditionChildren=" + conditionChildren +
                ", tableChildren=" + tableChildren +
                ", subSegments=" + subSegments +
                '}';
    }

    public void createTable(String tableName) {
        conditionColumnReference.put(tableName, new ArrayList<>());
        resultColumnReference.put(tableName, new ArrayList<>());
        joinColumnReference.put(tableName, new ArrayList<>());
        orderByColumnReference.put(tableName, new ArrayList<>());
        groupByColumnReference.put(tableName, new ArrayList<>());
    }

    public void createTable(Map<String, List<String>> columnReference) {
        doCreateTable(columnReference, getConditionColumnReference());
        doCreateTable(columnReference, getResultColumnReference());
        doCreateTable(columnReference, getJoinColumnReference());
        doCreateTable(columnReference, getGroupByColumnReference());
        doCreateTable(columnReference, getOrderByColumnReference());
    }

    private void doCreateTable(Map<String, List<String>> columnReferenceForSaveTable, Map<String, List<String>> columnReference) {
        Set<String> tableNames = columnReferenceForSaveTable.keySet();
        for (String tableName : tableNames) {
            if (!columnReference.containsKey(tableName)) {
                columnReference.put(tableName, new ArrayList<>());
            }
        }
    }


}

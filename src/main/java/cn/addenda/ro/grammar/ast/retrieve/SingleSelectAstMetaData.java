package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.AstMetaDataHelper;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.*;

/**
 * @author addenda
 * @datetime 2022/8/13 11:45
 */
public class SingleSelectAstMetaData extends AstMetaData {

    // 存返回的值
    private final List<Token> resultColumnList = new ArrayList<>();

    private final Map<String, Set<String>> resultColumnReference = new HashMap<>();
    private final Map<String, Set<String>> joinColumnReference = new HashMap<>();
    private final Map<String, Set<String>> groupByColumnReference = new HashMap<>();
    private final Map<String, Set<String>> orderByColumnReference = new HashMap<>();

    private Map<String, Curd> aliasTableMap = new HashMap<>();

    public SingleSelectAstMetaData() {
        super();
        resultColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new HashSet<>());
        joinColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new HashSet<>());
        groupByColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new HashSet<>());
        orderByColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new HashSet<>());
    }

    /**
     * 将astMetaData的conditionColumnMap合并到当前对象中
     */
    public void mergeColumnReference(SingleSelectAstMetaData astMetaData) {
        super.mergeColumnReference(astMetaData);
        AstMetaDataHelper.mergeColumnReference(astMetaData.getResultColumnReference(), resultColumnReference);
        AstMetaDataHelper.mergeColumnReference(astMetaData.getJoinColumnReference(), joinColumnReference);
        AstMetaDataHelper.mergeColumnReference(astMetaData.getGroupByColumnReference(), groupByColumnReference);
        AstMetaDataHelper.mergeColumnReference(astMetaData.getOrderByColumnReference(), orderByColumnReference);
    }

    @Override
    public void sortMetaData() {
        doSortMetaData(conditionColumnReference, aliasTableMap);
        doSortMetaData(resultColumnReference, aliasTableMap);
        doSortMetaData(joinColumnReference, aliasTableMap);
        doSortMetaData(groupByColumnReference, aliasTableMap);
        doSortMetaData(orderByColumnReference, aliasTableMap);
    }

    private void doSortMetaData(Map<String, Set<String>> columnReference, Map<String, Curd> aliasTableMap) {
        // 需要将表建到引用中去
        Set<String> aliasTableEntries = aliasTableMap.keySet();
        for (String tableName : aliasTableEntries) {
            columnReference.computeIfAbsent(tableName, item -> new HashSet<>());
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
            Set<String> strings = columnReference.get(UNDETERMINED_TABLE);
            for (String column : strings) {
                columnReference.get(tableName).add(column);
            }
            columnReference.get(UNDETERMINED_TABLE).clear();
        }
    }

    @Override
    public void createTable(String tableName) {
        super.createTable(tableName);
        resultColumnReference.computeIfAbsent(tableName, item -> new HashSet<>());
        joinColumnReference.computeIfAbsent(tableName, item -> new HashSet<>());
        orderByColumnReference.computeIfAbsent(tableName, item -> new HashSet<>());
        groupByColumnReference.computeIfAbsent(tableName, item -> new HashSet<>());
    }

    @Override
    public void createTable(Map<String, Set<String>> columnReference) {
        super.createTable(columnReference);
        doCreateTable(columnReference, resultColumnReference);
        doCreateTable(columnReference, joinColumnReference);
        doCreateTable(columnReference, groupByColumnReference);
        doCreateTable(columnReference, orderByColumnReference);
    }

    public Map<String, Set<String>> getResultColumnReference() {
        return resultColumnReference;
    }

    public Map<String, Set<String>> getJoinColumnReference() {
        return joinColumnReference;
    }

    public Map<String, Set<String>> getGroupByColumnReference() {
        return groupByColumnReference;
    }

    public Map<String, Set<String>> getOrderByColumnReference() {
        return orderByColumnReference;
    }

    public Map<String, Curd> getAliasTableMap() {
        return aliasTableMap;
    }

    public void setAliasTableMap(Map<String, Curd> aliasTableMap) {
        this.aliasTableMap = aliasTableMap;
    }

    public List<Token> getResultColumnList() {
        return resultColumnList;
    }

}

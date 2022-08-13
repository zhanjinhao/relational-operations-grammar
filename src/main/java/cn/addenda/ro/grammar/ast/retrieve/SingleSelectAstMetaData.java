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

    private final Map<String, List<String>> resultColumnReference = new HashMap<>();
    private final Map<String, List<String>> joinColumnReference = new HashMap<>();
    private final Map<String, List<String>> groupByColumnReference = new HashMap<>();
    private final Map<String, List<String>> orderByColumnReference = new HashMap<>();

    private Map<String, Curd> aliasTableMap = new HashMap<>();

    public SingleSelectAstMetaData() {
        super();
        resultColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new ArrayList<>());
        joinColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new ArrayList<>());
        groupByColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new ArrayList<>());
        orderByColumnReference.computeIfAbsent(UNDETERMINED_TABLE, item -> new ArrayList<>());
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

    private void doSortMetaData(Map<String, List<String>> columnReference, Map<String, Curd> aliasTableMap) {
        // 需要将表建到引用中去
        Set<String> aliasTableEntries = aliasTableMap.keySet();
        for (String tableName : aliasTableEntries) {
            columnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
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
    public void createTable(String tableName) {
        super.createTable(tableName);
        resultColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
        joinColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
        orderByColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
        groupByColumnReference.computeIfAbsent(tableName, item -> new ArrayList<>());
    }

    @Override
    public void createTable(Map<String, List<String>> columnReference) {
        super.createTable(columnReference);
        doCreateTable(columnReference, resultColumnReference);
        doCreateTable(columnReference, joinColumnReference);
        doCreateTable(columnReference, groupByColumnReference);
        doCreateTable(columnReference, orderByColumnReference);
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

    public List<Token> getResultColumnList() {
        return resultColumnList;
    }

}

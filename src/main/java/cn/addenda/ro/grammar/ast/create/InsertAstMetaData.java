package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.*;

/**
 * @author addenda
 * @datetime 2022/8/13 21:37
 */
public class InsertAstMetaData extends AstMetaData {

    // 存插入的字段
    private final List<Token> insertColumnList = new ArrayList<>();

    private String tableName;

    private final Map<String, Set<String>> onDuplicateUpdateReference = new HashMap<>();

    public void putTableName(String tableName) {
        createTable(tableName);
        this.tableName = tableName;
    }

    @Override
    public void sortMetaData() {
        Set<String> undeterminedColumnList = conditionColumnReference.get(UNDETERMINED_TABLE);

        conditionColumnReference.forEach((item, value) -> {
            if (!UNDETERMINED_TABLE.equals(item)) {
                value.addAll(undeterminedColumnList);
            }
        });

        undeterminedColumnList.clear();
    }

    public Map<String, Set<String>> getOnDuplicateUpdateReference() {
        return onDuplicateUpdateReference;
    }

    public List<Token> getInsertColumnList() {
        return insertColumnList;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "InsertAstMetaData{" +
                "tableName='" + tableName + '\'' +
                ", onDuplicateUpdateReference=" + onDuplicateUpdateReference +
                "} " + super.toString();
    }
}

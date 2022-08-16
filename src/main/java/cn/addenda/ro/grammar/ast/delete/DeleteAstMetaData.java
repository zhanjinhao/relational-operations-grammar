package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.AstMetaData;

import java.util.Set;

/**
 * @author addenda
 * @datetime 2022/8/13 18:12
 */
public class DeleteAstMetaData extends AstMetaData {

    private String tableName;

    @Override
    public AstMetaData getNewInstance() {
        return new DeleteAstMetaData();
    }

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

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "DeleteAstMetaData{" +
                "tableName='" + tableName + '\'' +
                "} " + super.toString();
    }

}

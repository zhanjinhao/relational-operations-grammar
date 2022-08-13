package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.AstMetaData;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/13 18:12
 */
public class DeleteAstMetaData extends AstMetaData {

    public void putTableName(String tableName) {
        createTable(tableName);
    }

    @Override
    public void sortMetaData() {
        List<String> undeterminedColumnList = conditionColumnReference.get(UNDETERMINED_TABLE);

        conditionColumnReference.forEach((tableName, value) -> {
            if (!UNDETERMINED_TABLE.equals(tableName)) {
                value.addAll(undeterminedColumnList);
            }
        });

        undeterminedColumnList.clear();
    }
}

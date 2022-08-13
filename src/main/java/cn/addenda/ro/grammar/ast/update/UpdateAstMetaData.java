package cn.addenda.ro.grammar.ast.update;

import cn.addenda.ro.grammar.ast.AstMetaData;

import java.util.Set;

/**
 * @author addenda
 * @datetime 2022/8/13 19:17
 */
public class UpdateAstMetaData extends AstMetaData {

    public void putTableName(String tableName) {
        createTable(tableName);
    }

    @Override
    public void sortMetaData() {
        Set<String> undeterminedColumnList = conditionColumnReference.get(UNDETERMINED_TABLE);

        conditionColumnReference.forEach((tableName, value) -> {
            if (!UNDETERMINED_TABLE.equals(tableName)) {
                value.addAll(undeterminedColumnList);
            }
        });

        undeterminedColumnList.clear();
    }
}

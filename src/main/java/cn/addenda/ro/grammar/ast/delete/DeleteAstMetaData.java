package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.ast.AstMetaData;

import java.util.Set;

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
        Set<String> undeterminedColumnList = conditionColumnReference.get(UNDETERMINED_TABLE);

        conditionColumnReference.forEach((item, value) -> {
            if (!UNDETERMINED_TABLE.equals(item)) {
                value.addAll(undeterminedColumnList);
            }
        });

        undeterminedColumnList.clear();
    }

}

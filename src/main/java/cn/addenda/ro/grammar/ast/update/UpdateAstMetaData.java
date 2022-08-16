package cn.addenda.ro.grammar.ast.update;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2022/8/13 19:17
 */
public class UpdateAstMetaData extends AstMetaData {

    /**
     * 存更新的字段
     */
    private final List<Token> updateColumnList = new ArrayList<>();

    private String tableName;

    @Override
    public AstMetaData getNewInstance() {
        return new UpdateAstMetaData();
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

    public List<Token> getUpdateColumnList() {
        return updateColumnList;
    }

    @Override
    public String toString() {
        return "UpdateAstMetaData{" +
            "tableName='" + tableName + '\'' +
            "} " + super.toString();
    }

}

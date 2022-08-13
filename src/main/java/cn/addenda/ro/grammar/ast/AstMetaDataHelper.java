package cn.addenda.ro.grammar.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author addenda
 * @datetime 2022/8/13 11:51
 */
public class AstMetaDataHelper {

    /**
     * 将thatConditionColumnMap合并到当前对象中
     */
    public static void mergeColumnReference(Map<String, List<String>> thatConditionColumnMap, Map<String, List<String>> thisConditionColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatConditionColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (thisConditionColumnMap.containsKey(key)) {
                thisConditionColumnMap.get(key).addAll(value);
            } else {
                thisConditionColumnMap.put(key, new ArrayList<>(value));
            }
        }
    }
}

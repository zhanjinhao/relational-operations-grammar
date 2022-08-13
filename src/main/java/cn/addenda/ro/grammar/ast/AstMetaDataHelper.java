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

    private AstMetaDataHelper() {
    }

    /**
     * 将 thatColumnMap 合并到 thisColumnMap 中
     */
    public static void mergeColumnReference(Map<String, List<String>> thatColumnMap, Map<String, List<String>> thisColumnMap) {
        Set<Map.Entry<String, List<String>>> entries = thatColumnMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (thisColumnMap.containsKey(key)) {
                thisColumnMap.get(key).addAll(value);
            } else {
                thisColumnMap.put(key, new ArrayList<>(value));
            }
        }
    }
}

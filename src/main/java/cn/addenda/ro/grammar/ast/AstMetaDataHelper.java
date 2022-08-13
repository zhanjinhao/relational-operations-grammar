package cn.addenda.ro.grammar.ast;

import java.util.HashSet;
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
     * 将 thatColumnReferenceMap 合并到 thisColumnReferenceMap 中
     */
    public static void mergeColumnReference(
            Map<String, Set<String>> thatColumnReferenceMap, Map<String, Set<String>> thisColumnReferenceMap) {
        Set<Map.Entry<String, Set<String>>> entries = thatColumnReferenceMap.entrySet();
        for (Map.Entry<String, Set<String>> entry : entries) {
            String key = entry.getKey();
            Set<String> value = entry.getValue();
            if (thisColumnReferenceMap.containsKey(key)) {
                thisColumnReferenceMap.get(key).addAll(value);
            } else {
                thisColumnReferenceMap.put(key, new HashSet<>(value));
            }
        }
    }
}

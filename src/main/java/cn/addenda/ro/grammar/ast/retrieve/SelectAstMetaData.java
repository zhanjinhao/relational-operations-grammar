package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.AstMetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/13 18:07
 */
public class SelectAstMetaData extends AstMetaData {

    // 存Select语句的SingleSelect集合
    private final List<AstMetaData> subSegments = new ArrayList<>();

    public List<AstMetaData> getSubSegments() {
        return subSegments;
    }

}

package cn.addenda.ro.grammar.ast.retrieve;

/**
 * @author addenda
 * @datetime 2021/12/10 22:04
 */
public enum SelectType {

    /**
     * 查询的结果作为视图使用：TOP TABLE INSERT
     */
    VIEW,
    /**
     * 查询的结果作为集合使用：IN EXISTS
     */
    LIST,
    /**
     * 查询的结果作为值使用：PRIMARY
     */
    VALUE

}
